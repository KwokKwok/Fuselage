# Sort

## Bubble

```C#
public static void Sort(this int[] array)
{
    for (int i = 0; i < array.Length - 1; i++)
    {
        for (int j = 0; j < array.Length - 1 - i; j++)
        {
            if (array[j] > array[j + 1])
            {
                array.Swap(j, j + 1);
            }
        }
    }
}
```

## Selection

```C#
public static void Sort(this int[] array)
{
    for (int i = 0; i < array.Length - 1; i++)
    {
        var temp = 0;
        for (int j = 1; j < array.Length - i; j++)
        {
            if (array[j] > array[temp])
            {
                temp = j;
            }
        }
        array.Swap(temp, array.Length - 1 - i);
    }
}
```

## Insert

```C#
public static void Sort(this int[] array)
{
    for (int i = 1; i < array.Length; i++)
    {
        var j = i;
        var temp = array[j];//要为这个数字找位置
        for (; j > 0 && array[j - 1] > temp; j--)
        {//不到0的位置，并且当前位置的数字比要排序的数字大，则将当前位置的数值向后挪一位
            array[j] = array[j - 1];
        }
        array[j] = temp;
    }
}
```

## Quick

```C#
public static void Sort(this int[] array, int low = 10, int high = -1)
{
    if (low == 10 && high == -1)
    {
        low = 0;
        high = array.Length - 1;
    }
    if (low < high)
    {
        var temp = array[low];
        int _low = low, _high = high;
        while (_low < _high)
        {
            while (_low < _high && array[_high] >= temp)
            {
                _high--;
            }
            array[_low] = array[_high];
            while (_low < _high && array[_low] <= temp)
            {
                _low++;
            }
            array[_high] = array[_low];
        }
        var mid = _low;
        array[mid] = temp;
        array.Sort(low, mid - 1);
        array.Sort(mid + 1, high);
    }
}
```

## Heap

```C#
public static void Sort(this int[] array)
{
    var maxIndex = array.Length - 1;
    for (int i = 0; i < maxIndex; i++)
    {
        var lastIndex = maxIndex - i;

        for (int j = (lastIndex - 1) / 2; j >= 0; j--)
        {
            var k = j;
            while (k * 2 + 1 <= lastIndex)
            {
                var biggerIndex = k * 2 + 1;
                if (biggerIndex < lastIndex && array[biggerIndex] < array[biggerIndex + 1])
                {
                    biggerIndex++;
                }
                if (array[k] < array[biggerIndex])
                {
                    array.Swap(k, biggerIndex);
                    k = biggerIndex;
                }
                else
                {
                    break;
                }
            }
        }

        array.Swap(0, lastIndex);
    }
}
```

## Shell

```C#
public static void Sort(this int[] array)
{
    int j, temp = 0;
    for (int increment = array.Length / 2; increment > 0; increment /= 2)
    {
        for (int i = increment; i < array.Length; i++)
        {
            temp = array[i];
            for (j = i; j >= increment; j -= increment)
            {
                if (temp < array[j - increment])
                {
                    array[j] = array[j - increment];
                }
                else
                {
                    break;
                }
            }
            array[j] = temp;
        }
    }
}
```

## Merge

```C#
public static void Sort(this int[] array, int low = 10, int high = -1)
{
    if (low == 10 && high == -1)
    {
        low = 0;
        high = array.Length - 1;
    }
    var mid = (high + low) / 2;
    if (low < high)
    {
        array.Sort(low, mid);
        array.Sort(mid + 1, high);

        var temp = new int[high - low + 1];
        var i = low;
        var j = mid + 1;
        int k = 0;

        while (i <= mid && j <= high)
        {
            if (array[i] < array[j])
            {
                temp[k++] = array[i++];
            }
            else
            {
                temp[k++] = array[j++];
            }
        }

        while (i <= mid)
        {
            temp[k++] = array[i++];
        }

        while (j <= high)
        {
            temp[k++] = array[j++];
        }

        for (int k2 = 0; k2 < temp.Length; k2++)
        {
            array[k2 + low] = temp[k2];
        }
    }
}
```
