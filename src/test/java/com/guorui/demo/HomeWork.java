package com.guorui.demo;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HomeWork {

   static void quickSort(int[] arr,int low,int high){
        int i,j,temp,t;
        if(low>high){
            return;
        }
        i=low;
        j=high;
        temp = arr[low];
        while (i<j) {
            while (temp<=arr[j]&&i<j) {
                j--;
            }
            while (temp>=arr[i]&&i<j) {
                i++;
            }
            if (i<j) {
                t = arr[j];
                arr[j] = arr[i];
                arr[i] = t;
            }

        }
        arr[low] = arr[i];
        arr[i] = temp;
        quickSort(arr, low, j-1);
        quickSort(arr, j+1, high);
    }


    public static float hornor1(float x,int n,float[] A){
       //递归方法求多项式 P(x,A)=A[n]*x^n+...+A[i]*x^i+A[0]*x^0 的值
        if (n == A.length - 1){
            return A[n];
        }else{
            return hornor1(x,n+1,A)*x + A[n];
        }
    }

    public static String[] grayCode(int n){
       if (n==1){
           return new String[]{"0","1"};
       }else{
           int index;
           int sign = 1;
           for (int x=0;x<n;x++){
               sign *= 2;
           }
           String[] temp = grayCode(n-1);
           String[] result = new String[sign];
           for (index=0;index<result.length/2;index++){
               result[index] = "0" + temp[index];
           }
           for (;index<result.length;index++){
               result[index] = "1"+temp[index-sign/2];
           }
           return result;
       }
    }

    public static int Marjority(int[] num){
        int count = 0;
        int basicValue = num[0];
        for (int index=0;index<num.length;index++){
            if (num[index] == basicValue){
                //出现相同
                count ++;
            }else{
                count --;
            }
            if (count == 0 && index != num.length-1){
                basicValue = num[index+1];
            }
        }
        if (count < 1){
            return -1;
        }
        return basicValue;
    }

    public static int MarjorityRe(int num[]){
       int count =0;
       for (int index=0;index<num.length;index++){
           if (num[index] == num[0]){
               count++;
           }else{
               count--;
           }
           if (count == 0){
               return MarjorityRe(Arrays.copyOfRange(num,index+1,num.length));
           }
       }
       if (count < 1){
           return -1;
       }
       return num[0];
    }

    public static void main(String[] args) {
        String test = "guorui";
        char[] cs = test.toCharArray();

        System.out.println(Arrays.toString(cs));
        HashMap<Character,Integer> hashMap = new HashMap();
        for (int index=0;index<cs.length;index++){
            if (hashMap.put(cs[index],1) != null){
                hashMap.put(cs[index],hashMap.get(cs[index]) + 1);
            }
        }
        System.out.println(hashMap.toString());
   }




}
