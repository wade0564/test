package cn.edu.ustc.alogrithm;
 
/**
 * 各种排序算法
 *
 * @author huwei
 *
 */
public class Sort {
    public static void main(String[] args) {
        int[] a = { 60, 57, 89, 47, 57, 98, 45, 35, 73 };
        Sort sort = new Sort();
        sort.quickSort(a);
        for (int i = 0; i < a.length; i++) {
            System.out.print(" " + a[i] + " ");
        }
    }
 
    /**
     * 插入排序
     *
     * @param data
     */
    public void insertSort(int[] data) {
        for (int i = 1; i < data.length; i++) {
            for (int j = i; (j > 0) && (data[j] < data[j - 1]); j--) {
                swap(data, j, j - 1);
            }
        }
    }
 
    /**
     * 冒泡排序
     *
     * @param data
     */
    public void bubbleSort(int[] data) {
        for (int i = 0; i < data.length; i++) {
            for (int j = data.length - 1; j > i; j--) {
                if (data[j] < data[j - 1]) {
                    swap(data, j, j - 1);
                }
            }
        }
 
    }
 
    /**
     * 选择排序
     *
     * @param data
     */
    public void selectSort(int[] data) {
        for (int i = 0; i < data.length; i++) {
            int lowIndex = i;
            for (int j = data.length - 1; j > i; j--) {
                if (data[j] < data[lowIndex]) {
                    lowIndex = j;
                }
            }
            swap(data, i, lowIndex);
        }
    }
 
    /**
     * shell排序
     *
     * @param data
     */
    public void shellSort(int[] data) {
        for (int i = data.length / 2; i > 2; i /= 2) {
            for (int j = 0; j < i; j++) {
                insertSort(data, j, i);
            }
        }
        insertSort(data, 0, 1);// 最后进行一次插入排序
    }
 
    private void insertSort(int[] data, int start, int inc) {
        for (int i = start + inc; i < data.length; i += inc) {
            for (int j = i; (j >= inc) && (data[j] < data[j - inc]); j -= inc) {
                swap(data, j, j - inc);
            }
        }
    }
 
    /**
     * 快速排序 重点
     *
     * @param data
     */
    public void quickSort(int[] data) {
        quickSort(data, 0, data.length - 1);
    }
 
    private void quickSort(int[] data, int i, int j) {
        int pivotIndex = (i + j) / 2;
        // swap
        swap(data, pivotIndex, j);
 
        int k = partition(data, i - 1, j, data[j]);
        swap(data, k, j);
        if ((k - i) > 1)
            quickSort(data, i, k - 1);
        if ((j - k) > 1)
            quickSort(data, k + 1, j);
 
    }
 
    private int partition(int[] data, int l, int r, int pivot) {
        do {
            while (data[++l] < pivot)
                ;
            while ((r != 0) && data[--r] > pivot)
                ;
            swap(data, l, r);
        } while (l < r);
        swap(data, l, r);
        return l;
    }
 
    /**
     * 归并排序
     *
     * @param data
     */
    public void mergeSort(int[] data) {
        int[] temp = new int[data.length];
        mergeSort(data, temp, 0, data.length - 1);
    }
 
    private void mergeSort(int[] data, int[] temp, int l, int r) {
        int mid = (l + r) / 2;
        if (l == r)
            return;
        mergeSort(data, temp, l, mid);
        mergeSort(data, temp, mid + 1, r);
        for (int i = l; i <= r; i++) {
            temp[i] = data[i];
        }
        int i1 = l;
        int i2 = mid + 1;
        for (int cur = l; cur <= r; cur++) {
            if (i1 == mid + 1)
                data[cur] = temp[i2++];
            else if (i2 > r)
                data[cur] = temp[i1++];
            else if (temp[i1] < temp[i2])
                data[cur] = temp[i1++];
            else
                data[cur] = temp[i2++];
        }
    }
 
    /**
     * 堆排序
     *
     * @param data
     */
    public void heapSort(int[] data) {
        MaxHeap h = new MaxHeap();
        h.init(data);
        for (int i = 0; i < data.length; i++)
            h.remove();
        System.arraycopy(h.queue, 1, data, 0, data.length);
    }
 
    private static class MaxHeap {
 
        void init(int[] data) {
            this.queue = new int[data.length + 1];
            for (int i = 0; i < data.length; i++) {
                queue[++size] = data[i];
                fixUp(size);
            }
        }
 
        private int size = 0;
 
        private int[] queue;
 
    /*  public int get() {
            return queue[1];
        }*/
 
        public void remove() {
            swap(queue, 1, size--);
            fixDown(1);
        }
 
        // fixdown
        private void fixDown(int k) {
            int j;
            while ((j = k << 1) <= size) {
                if (j < size && queue[j] < queue[j + 1])
                    j++;
                if (queue[k] > queue[j]) // 不用交换
                    break;
                swap(queue, j, k);
                k = j;
            }
        }
 
        private void fixUp(int k) {
            while (k > 1) {
                int j = k >> 1;
                if (queue[j] > queue[k])
                    break;
                swap(queue, j, k);
                k = j;
            }
        }
 
    }
 
    public static void swap(int[] data, int i, int j) {
        int temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }
}