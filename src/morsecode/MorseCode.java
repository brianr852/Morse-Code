/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package morsecode;

/**
 *
 * @author brian
 */
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.io.*;

/**
 * A class that implements text encoding/decoding with Morse Code
 *
 * @author Brian Radomski
 * @version 11/11/14
 */
public class MorseCode
{
    private TreeNode root;

    public MorseCode()
    {
        this.root = new TreeNode(' ');
    }

    private void buildMorseCodeTree()
    {
        int index;
        TreeNode current;
        TreeNode letterTree;
        String code = null;
        try
        {
            Scanner file = new Scanner(new File("MorseCode.txt"));
            System.out.println("The Morse Code:");
            System.out.println("===============");

            while (file.hasNext()) // test for the end of the file
            {
                code = file.nextLine();  // read a line
                System.out.println(code);  // print the line read
                // building the tree
                letterTree = new TreeNode(code.charAt(0));
                current = this.root;
                index = 2;
                for (; index < code.length() - 1; index++)
                {
                    if (code.charAt(index) == '.')
                    {
                        current = current.getLeft();
                    } else // must be '_'
                    {
                        current = current.getRight();
                    }
                }
                if (code.charAt(index) == '.')
                {
                    current.setLeft(letterTree);
                } else // must be '_'
                {
                    current.setRight(letterTree);
                }
            }
            file.close();
        } catch (FileNotFoundException fnfe)
        {
            System.out.println("Unable to find MorseCode.txt, exiting");
        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    private void decode(String encoded)
    {
        System.out.println("Decoding \"" + encoded + "\"");
        TreeNode current = this.root;
        String answer = "";
        StringTokenizer decoded = new StringTokenizer(encoded, " ");

        // YOUR CODE GOES HERE
        try {
            while (decoded.hasMoreTokens())
            {
                String letter = decoded.nextToken();
                for (int i = 0; i < letter.length(); i++)
                {
                    if (letter.charAt(i) == '_')
                    {
                        current = current.getRight();
                    }
                    else
                    {
                        current = current.getLeft();
                    }
                }
                answer += current.getData();
                current = root;
            }


            System.out.println("The decoded string is \"" + answer + "\"");
        }
        catch (NullPointerException npe)
        {
            System.out.println("Not a Morse pattern.");
        }
    }

    public LevelOrderIterator getLevelOrderIterator()
    {
        return new LevelOrderIterator();
    } // end getLevelOrderIterator

    private class TreeNode
    {
        private char data;
        private TreeNode left;
        private TreeNode right;

        public TreeNode(char dataPortion)
        {
            this.data = dataPortion;
            this.left = null;
            this.right = null;
        } // end constructor

        public void setData(char newData)
        {
            this.data = newData;
        } // end setData

        public char getData()
        {
            return this.data;
        } // end getData

        public TreeNode getLeft()
        {
            return this.left;
        } // end getLeft

        public void setLeft(TreeNode newLeft)
        {
            this.left = newLeft;
        } // end setLeft

        public boolean hasLeft()
        {
            return this.left != null;
        } // end hasLeft

        public TreeNode getRight()
        {
            return this.right;
        } // end getRight

        public void setRight(TreeNode newRight)
        {
            this.right = newRight;
        } // end setRight

        public boolean hasRight()
        {
            return this.right != null;
        } // end hasRight
    } // end TreeNode


    private class LevelOrderIterator implements Iterator
    {
        private LinkedBlockingQueue<TreeNode> nodeQueue;

        public LevelOrderIterator()
        {
            this.nodeQueue = new LinkedBlockingQueue<TreeNode>();
            if (root != null)
            {
                this.nodeQueue.offer(root);
            }
        } // end default constructor

        public boolean hasNext()
        {
            return !this.nodeQueue.isEmpty();
        } // end hasNext

        public TreeNode next()
        {
            TreeNode nextNode;

            if (hasNext())
            {
                nextNode = this.nodeQueue.poll();
                TreeNode leftChild = nextNode.getLeft();
                TreeNode rightChild = nextNode.getRight();

                // add to queue in order of recursive calls
                if (leftChild != null)
                    this.nodeQueue.offer(leftChild);

                if (rightChild != null)
                    this.nodeQueue.offer(rightChild);
            } else
            {
                throw new NoSuchElementException();
            }

            return nextNode;
        } // end next

        public void remove()
        {
            throw new UnsupportedOperationException();
        } // end remove
    } // end LevelOrderIterator


    public static void main(String[] args)
    {
        MorseCode morseCode = new MorseCode();
        morseCode.buildMorseCodeTree();

        LevelOrderIterator iter = morseCode.getLevelOrderIterator();
        System.out.println("\nThe Morse Code Tree in the level order:");
        while (iter.hasNext())
        {
            System.out.print(iter.next().getData() + " ");
        }
        System.out.println("\n\n");

        String response = "";
        Scanner keyboard = new Scanner(System.in);
        do
        {
            do
            {
                System.out.println("Please enter a message in Morse Code, use space as a separator.");
                response = keyboard.nextLine();
            } while (!response.matches("[._ ]+"));

            morseCode.decode(response);
            System.out.println("Would you like to decode another message? (yes/no)");
            response = keyboard.nextLine();
        } while (response.equalsIgnoreCase("yes"));
    }
}