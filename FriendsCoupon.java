import java.util.*;
import java.io.*;
public class FriendsCoupon
{
    public static void main(String[] args) throws IOException
    {
        if(args.length < 1)
        {
            System.out.println("Error, usage: java ClassName inputfile");
            System.exit(1);
        }        
        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        String numString = "";
        try 
        {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) 
            {
                sb.append(line);
                line = br.readLine();
            }
            numString = sb.toString();
        } 
        finally 
        {
            br.close();
        }
        int numCoupons = Integer.parseInt(args[1]);
        if(numCoupons < 1 || numCoupons > 26)
        {
            System.out.println("Error: the number of coupons needs to be between 1 and 26 (inclusive).");;
        }
        String[] alphabet = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        String[] couponList = new String[numCoupons];
        for(int i = 0; i < couponList.length; i++)
        {
            couponList[i] = alphabet[i];
        }
        int counter = 0;
        numString = numString.replaceAll("\\s+","");
        for(int i = 0; i < numString.length(); i++)
        {
            counter++;
        }
        int x = 0;
        if((Math.sqrt(counter)-(int)(Math.sqrt(counter)) == 0))
        {
            x = (int)Math.sqrt(counter);
        }
        else
        {
            System.out.println("Error, a square table cannot be constructed with the contents of this file");
        }
        int[][] friendsTable = new int[x][x];
        for(int i = 0; i < x; i++)
        {
            for(int j = 0; j < x; j++)
            {
                friendsTable[i][j] = Integer.parseInt(numString.substring(((x*i)+j),((x*i)+j+1)));
            }
        }    
        for(int i = 0; i < x; i++)
        {
            for(int j = 0; j < x; j++)
            {
                if(friendsTable[i][i] == 1)
                {
                    System.out.println("Error: invalid table, user " + i + " cannot be friends with itself.");
                    System.exit(0);
                }
                if(friendsTable[i][j] != friendsTable[j][i])
                {
                    System.out.println("Error: invalid table");
                    System.exit(0);
                }
            }
        }   
        String[] solution = new String[x];
        for(int i = 0; i < solution.length; i++)
        {
            solution[i] = "";
        }
        solve(friendsTable, couponList, solution);
        System.out.println("There is no way to assign " + numCoupons + " coupon(s) to " + x + " users.");
    }
    
    public static boolean reject(int[][] table, String[] partial)
    {
        boolean rejected = false;
        search://once the loop finds the first case where it should be rejected, it stops searching and returns 'rejected' which is set to true
        for(int i = 0; i < table.length; i++)
        {
            for(int j = 0; j < table[i].length; j++)
            {
                if((table[i][j] == 1) && (partial[i].equals(partial[j])) && (!(partial[i].equals(""))) && (!(partial[j].equals(""))))
                {
                    rejected = true;
                    break search;
                }
            }
        }
        return rejected;
    }
    
    public static void testRejectUnit(int[][] table, String[] test)
    {
        if(reject(table,test))
        {
            System.out.println("Rejected: " + Arrays.toString(test));
        }
        else
        {
            System.out.println("Not rejected: " + Arrays.toString(test));
        }
    }
    
    public static void testReject()
    {
        //Should not be rejected
        testRejectUnit((new int[][] {{0,1,1,0},{1,0,0,0},{1,0,0,1},{0,0,1,0}}), (new String[] {"A","B","C","A"}));//users 1 and 4 have the same coupon, but aren't friends
        testRejectUnit((new int[][] {{0,1,1,1},{1,0,1,1},{1,1,0,1},{1,1,1,0}}), (new String[] {"A","B","C","D"}));//all users are friends, but have different coupons
        testRejectUnit((new int[][] {{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}}), (new String[] {"A","A","A","A"}));//all users have same coupon, but aren't friends
        testRejectUnit((new int[][] {{0,1,1,1},{1,0,1,1},{1,1,0,1},{1,1,1,0}}), (new String[] {"","","",""}));//all users are friends, but have not been assigned coupons
        testRejectUnit((new int[][] {{0,0,1,0},{0,0,0,1},{0,0,0,0},{0,0,0,0}}), (new String[] {"A","B","C",""}));//1 is friends with 3, but they have different coupons
        System.out.println();
        //Should be rejected
        testRejectUnit((new int[][] {{0,1,1,0},{1,0,1,0},{1,1,0,1},{0,0,1,0}}), (new String[] {"A","B","B","A"}));//2 is friends with 3, and they have the same coupon
        testRejectUnit((new int[][] {{0,1,1,1},{1,0,1,1},{1,1,0,1},{1,1,1,0}}), (new String[] {"A","A","A","A"}));//users are all friends and all have the same coupon
        testRejectUnit((new int[][] {{0,1,1,1},{1,0,1,1},{1,1,0,1},{1,1,0,1}}), (new String[] {"A","A","A","A"}));//all users are friends and have the same coupons
        testRejectUnit((new int[][] {{0,0,1,0},{0,0,0,0},{1,0,0,0},{0,0,0,0}}), (new String[] {"A","B","A","B"}));//1 is friends with 3, and they have the same coupon
        testRejectUnit((new int[][] {{0,0,1,0},{0,0,0,1},{1,0,0,0},{0,1,0,0}}), (new String[] {"A","B","A",""}));//1 is friends with 3, and they have the same coupon
    }    
    
    public static String[] next(String[] coupons, String[] partial)
    {
        String[] temp = new String[partial.length];
        for(int i = 0; i < partial.length; i++)
        {
            temp[i] = partial[i];
        }
        int nextBlank = 0;
        for(int i = 0; i < temp.length; i++)
        {
            if(temp[i].equals(""))
            {
                nextBlank = i;
                break;
            }
            else
            {
                nextBlank = -1;
            }
        }
        if(nextBlank > 0)
        {
            for(int i = 0; i < coupons.length; i++)
            {
                if(temp[0].equals(""))
                {
                    temp[0] = coupons[0];
                    break;
                }
                if(i+1 >= coupons.length)
                {
                    temp = null;
                    break;
                }
                else if(temp[nextBlank-1].equals(coupons[i]))
                {
                    temp[nextBlank-1] = coupons[i+1];
                    break;
                }
            }            
        }
        else if(nextBlank <= 0)
        {
            for(int i = 0; i < coupons.length; i++)
            {
                if(temp[0].equals(""))
                {
                    temp[0] = coupons[0];
                    break;
                }
                if(i+1 >= coupons.length)
                {
                    temp = null;
                    break;
                }
                else if(temp[temp.length-1].equals(coupons[i]))
                {
                    temp[temp.length-1] = coupons[i+1];
                    break;
                }
            }
        }
        return temp;
    }
    
    public static void testNextUnit(String[] coupons, String[] partial)
    {
        System.out.println("Updated " + Arrays.toString(partial) + " to " + Arrays.toString((next(coupons, partial))));
    }
    
    public static void testNext()
    {
        System.out.println("Can be nexted:");
        testNextUnit((new String[] {"A","B","C","D"}), (new String[] {"","","",""}));//Should next to "A","","",""
        testNextUnit((new String[] {"A","B","C","D"}), (new String[] {"A","","",""}));//Should next to "B","","",""
        testNextUnit((new String[] {"A","B","C","D"}), (new String[] {"A","B","",""}));//Should next to "A","C","",""
        testNextUnit((new String[] {"A","B","C","D"}), (new String[] {"A","B","C",""}));//Should next to "A","B","D",""
        testNextUnit((new String[] {"A","B","C","D"}), (new String[] {"A","B","C","C"}));//Should next to "A",B","C","D"
        testNextUnit((new String[] {"A","B","C","D"}), (new String[] {"A","B","C","A"}));//Should next to "A","B","C","B"
        testNextUnit((new String[] {"A","B"}), (new String[] {"A","A","",""}));//Should next to "A","B","",""
        testNextUnit((new String[] {"A","B"}), (new String[] {"A","B","A",""}));//Should next to "A","B","B",""
        testNextUnit((new String[] {"A","B"}), (new String[] {"","","",""}));//Should next to "A","","",""
        testNextUnit((new String[] {"A","B","C","D","E","F","G","H","I","J","K","L"}), (new String[] {"C","G","I","","","",""}));//Should next to "C","G","J","","","",""
        testNextUnit((new String[] {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"}), (new String[] {"C","G","I","Y","","",""}));//Should next to "C","G","I","Z","","",""
        System.out.println();
        System.out.println("Can't be nexted:");
        testNextUnit((new String[] {"A","B","C","D"}), (new String[] {"A","B","C","D"}));//"D" can't be nexted with given coupon list: return null        
        testNextUnit((new String[] {"A","B"}), (new String[] {"A","B","B",""}));//"B" can't be nexted with given coupon list: return null
        testNextUnit((new String[] {"A","B","C"}), (new String[] {"A","C","C","C"}));//"C" can't be nexted with given coupon list: return null                
        testNextUnit((new String[] {"A","B","C","D"}), (new String[] {"A","B","D",""}));//"D" can't be nexted with given coupon list: return null
        testNextUnit((new String[] {"A","B","C"}), (new String[] {"A","C"}));//"C" can't be nexted with given coupon list: return null
        testNextUnit((new String[] {"A","B","C","D"}), (new String[] {"D","","",""}));//"D" can't be nexted with given coupon list: return null
        testNextUnit((new String[] {"A"}), (new String[] {"A","","",""}));//"A" can't be nexted with given coupon list: return null
        testNextUnit((new String[] {"A"}), (new String[] {"A","A"}));//"A" can't be nexted with given coupon list: return null                
        testNextUnit((new String[] {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"}), (new String[] {"C","G","I","Z","","",""}));//"Z" can't be nexted with given coupon list: return null
        testNextUnit((new String[] {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y"}), (new String[] {"C","G","I","W","Q","A","Y"}));//"Y" can't be nexted with given coupon list: return null
    }
    
    public static boolean isFullSolution(int[][] table, String[] coupons, String[] partial)
    {
        boolean isFull = true;
        //method originally contained an if statement that checked for validity with reject(), but this is already done in solve() and including it here as well made my code redundant and slower. Deleting it did not change coupon assignments when the program was run.
        for(int i = 0; i < partial.length; i++)
        {
            if(partial[i].equals(""))
            {
                isFull = false;
                break;
            }
        }
        return isFull;
    }
    
    public static void testIsFullSolutionUnit(int[][] table, String[] coupons, String[] partial)
    {
        if(isFullSolution(table, coupons, partial))
        {
            System.out.println("Full solution: " + Arrays.toString(partial));
        }
        else
        {
            System.out.println("Not a full solution: " + Arrays.toString(partial));
        }
    } 
    
    public static void testIsFullSolution()
    {
        testIsFullSolutionUnit((new int[][] {{0,1,1,0},{1,0,0,0},{1,0,0,1},{0,0,1,0}}), (new String[] {"A","B","C"}), (new String[] {"A","B","C","A"}));//full
        testIsFullSolutionUnit((new int[][] {{0,1,1,0},{1,0,0,0},{1,0,0,1},{0,0,1,0}}), (new String[] {"A","B","C","D"}), (new String[] {"A","B","C",""}));//not full
        testIsFullSolutionUnit((new int[][] {{0,1,1,0},{1,0,0,0},{1,0,0,1},{0,0,1,0}}), (new String[] {"A","B","C","D"}), (new String[] {"A","A","",""}));//not full
        testIsFullSolutionUnit((new int[][] {{0,1,1,0},{1,0,0,0},{1,0,0,1},{0,0,1,0}}), (new String[] {"A","B","C","D"}), (new String[] {"","","",""}));//not full
    }
    
    public static String[] extend(int[][] table, String[] coupons, String[] partial)
    {        
        int index = -1;
        String[] temp = new String[partial.length];
        for(int i = 0; i < partial.length; i++)
        {
            temp[i] = partial[i];
        }
        for(int i = 0; i < partial.length; i++)
        {
            if(temp[i].equals(""))
            {
                temp[i] = coupons[0];
                index = i;
                break;
            }
        }
        if(index == -1)
        {
            temp = null;
        }
        return temp; 
    } 
    
    public static void testExtendUnit(int[][] table, String[] coupons, String[] partial)
    {
        System.out.println(Arrays.toString(partial) + " extended to " + Arrays.toString(extend(table, coupons, partial)));
    }
    
    public static void testExtend()
    {
        //Can extend
        System.out.println("Can be extended:");
        testExtendUnit((new int[][] {{0,1,1,0},{1,0,0,0},{1,0,0,1},{0,0,1,0}}), (new String[] {"A","B","C","D"}), (new String[] {"A","B","",""}));//Should extend to "A","B","A",""
        testExtendUnit((new int[][] {{0,0,0,0},{1,0,1,0},{1,0,0,1},{0,0,1,0}}), (new String[] {"A","B"}), (new String[] {"A","B","",""}));//Should extend to "A","B","A",""
        testExtendUnit((new int[][] {{0,0,1,0},{1,0,1,0},{1,0,0,1},{0,0,1,0}}), (new String[] {"A","B","C"}), (new String[] {"A","B","C",""}));//Should extend to "A","B","C","A"
        testExtendUnit((new int[][] {{0,1,1,1},{1,0,1,1},{1,1,0,1},{1,1,1,0}}), (new String[] {"A","B","C","D"}), (new String[] {"A","","",""}));//Should extend to "A","A","",""
        testExtendUnit((new int[][] {{0,1,1,0},{1,0,0,0},{1,0,0,1},{0,0,1,0}}), (new String[] {"A","B"}), (new String[] {"","","",""}));//Should extend to "A","","",""
        testExtendUnit((new int[][] {{0,1,1,0},{1,0,0,0},{1,0,0,1},{0,0,1,0}}), (new String[] {"A","B","C","D","E","F","G","H"}), (new String[] {"","","","","","","","","","","","","","","",""}));//Should extend to "A","","","","","","","","","","","","","","",""
        System.out.println();
        //Can't extend
        System.out.println("Can't be extended:");
        testExtendUnit((new int[][] {{0,1,1,0},{1,0,0,0},{1,0,0,1},{0,0,1,0}}), (new String[] {"A","B","C","D"}), (new String[] {"A","B","C","D"}));//Can't extend, already full: return null
        testExtendUnit((new int[][] {{0,0,1,0},{1,0,1,0},{1,0,0,1},{0,0,1,0}}), (new String[] {"A","B","C","D"}), (new String[] {"A","B"}));//Can't extend, partial is already full: return null
    }
    
    public static void solve(int[][] table, String[] coupons, String[] partial)
    {        
        if(reject(table, partial))
        {
            return;
        }
        if(isFullSolution(table, coupons, partial))
        {
            for(int i = 0; i < partial.length; i++)
            {
                System.out.println("User " + (i+1) + " assigned coupon " + partial[i]);
            }
            System.exit(0);
        }
        String[] attempt = extend(table, coupons, partial);
        while(attempt != null)
        {            
            solve(table, coupons, attempt);
            attempt = next(coupons, attempt);
        }         
    }
    
    public static void testSolve()
    {
        solve((new int[][] {{0,1,1,0},{1,0,0,0},{1,0,0,1},{0,0,1,0}}), (new String[] {"A","B"}), (new String[] {"","","",""}));
    }
}
