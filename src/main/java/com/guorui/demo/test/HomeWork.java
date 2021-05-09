package com.guorui.demo.test;


import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class HomeWork {

    //快速排序
    public static void quickSort(int n, int[] a)
    {
        int i, temp,j;
        for(i=0;i<n;i++)
        {
            for(j=i+1;j<n;j++)
            {
                if(a[i]>a[j])
                {
                    temp=a[i];
                    a[i]=a[j];
                    a[j]=temp;
                }
            }
        }
    }

    //寻找中项
    public static void find(int A[], int n, int k)
    {
        int i, j;
        int s = 0;
        int[] B = new int[44];
        for(i=1; i<n; i++)
        {
            for(j=0;j<i; j++)
            {
                if(A[j]==A[i])
                    break;
                if(j==i-1)
                    B[s++]=A[i];
            }
        }
        for(i=0;i<k;i++)
            System.out.println(B[i]);
    }

    public static int select(int k, int S[], int num)
    {
        int num1, num2,i,j,m,n1,n2,n3;
        int[] temp = new int[5];
        int[] M = new int[44];
        int[] S1 = new int[44];
        int[] S2 = new int[44];
        int[] S3 = new int[44];
        if(num<44)
        {
            quickSort(num,S);
            return S[k-1];
        }
        else
        {
            num1=num/5;
            j=0;
            num2=0;
            for(i=0;i<num1*5;i++)
            {
                temp[j]=S[i];
                j++;
                if(j==5)
                {j=0;
                    quickSort(5,temp);
                    M[num2]=temp[2];
                    num2++;
                }
            }
        }
        m=select((num1+1)/2,M,num2);
        n1=n2=n3=0;
        for(i=0;i<num;i++)
        {
            if(S[i]<m)
            {
                S1[n1]=S[i];
                n1++;
            }
            else if(S[i]==m)
            {
                S2[n2]=S[i];
                n2++;
            }
            else
            {
                S3[n3]=S[i];
                n3++;
            }
        }
        if(n1>k)
            return(select(k,S1,n1));
        else if(n1+n2>=k)
            return m;
        else return(select(k-n1-n2,S3,n3));
    }

    public static void main(String[] args) {
            int m, i, t, j;
            int d[] = new int[]{8 ,33 ,28, 17, 51, 57, 49, 35, 11, 25, 37, 14, 3 ,2, 13, 52, 12, 6, 2 ,32, 54, 5, 16, 22, 23, 7};
            quickSort(d.length-1, d);
            System.out.println(Arrays.toString(d));
            Scanner scanner = new Scanner(System.in);
        System.out.println("输入k(输出前k小元素)");
        int k = Integer.parseInt(scanner.nextLine());
        System.out.println("前" + k + "小元素:");
        for (int x=0;x<k;x++){
            System.out.print(d[x] + ",");
        }
    }

    static class Data{
        int number;
        int elem[] = new int[44];
        int k;
        int output[] = new int[44];
    }

}
