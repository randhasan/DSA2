# CS 1501 – Lab #6 (PHPArray)

## Table of Contents

- [Overview](#overview)
- [Arrays in PHP](#arrays-in-php)
- [PHPArray.java](#phparrayjava)
- [Testing](#testing)

## Overview

* __Purpose__:  In this lab you will complete the implementation of the __`PHPArray`__ class, a Java equivalent of the Array data structure used in the PHP programming language.

A starter project has been provided in this repository, with the following
directory contents.

```bash
├── PHPArray.java
├── README.md
├── docs/
└── output.txt
```

 In addition to the project files in the root directory, you can refer to the
 __`javadoc`__ documentation of the __`PHPArray`__ class inside
 __`docs/index.html`__ (you may safely disregard any other file within the
 __`docs/`__ subdirectory). It is an auto-generated class description webpage
 intended to provide neatly structured API documentation for developers, using the
 contents of __`@`__-tagged comment blocks within the source code. This is strictly
 optional however, and sometimes its easier to just read the comment blocks
 directly.

There are a total of __two__ code implementation tasks, each defined inside
numbered __`@TODO`__ objective comment blocks.

Implementation and test code will all be contained inside __`PHPArray.java`__, and
you can compare the sample test results against __`output.txt`__, the text printout
of the expected outcome.

## Arrays in PHP

In PHP, an array can behave in three different manners: Numerically Indexed,
Associative, and Multidimensional. The first type is the one we are all familiar
with and is the one we generally imagine when thinking about arrays `(A[i] = v)`.

However, in PHP an array can also be defined as what is practically an ordered map.
Initially this might sound strange, because normally a map is an unordered data
type that associate specific values to keys (ex. Java Map Interface, Dictionaries
before Python 3.7, Javascript Objects). The semantics of ordering only apply to
data types that provide concrete iteration logic; a conventional map data type
neither requires nor specifies internal ordering among its (key, value) pair
elements.

An associative PHP array, therefore, is essentially a hash table of (key, value) mappings. Keys can either be an integer or a string (but will happily cast other primitive data types like booleans, floats, NULL to either one), and values can be anything. For example, in PHP you can declare an array like such.

```php
$arr1 = array(
    "foo" => "bar",
    "bar" => "foo",
    100   => -100,
    -100  => 100,
);
var_dump($arr1);

```

The above example will output:
```php
array(4) {
	["foo"] => string(3) "bar"
	["bar"] => string(3) "foo"
	[100]   => int(-100)
	[-100]  => int(100)
}
```

The array above is both ordered (forward traversing as-is), and associative (`array["bar"] == "foo"`). Therefore, if we try to declare a PHP array with (key, value) pairs that have (or evaluate to) the same key, the latest pair's value will overwrite any previous value assignments for that key.

```php
// Type-casted association overwriting in PHP arrays.

$arr2 = array(
    1    => "a",
    "1"  => "b",
    1.5  => "c",
    true => "d",
);
var_dump($arr2);

```

The above will output:

```php
array(1) {
	[1] => string(1) "d"
}

```

What happened behind the scenes was this:

```php
$arr2 = array(
    1    => "a", // 1 => "a"
    "1"  => "b", // (int) "1"  => "b" = (1 => "b")
    1.5  => "c", // (int) 1.5  => "c" = (1 => "c")
    true => "d", // (int) true => "d" = (1 => "d")
);

```

So it seems like PHP arrays just cast every key into an integer and maps the given value to it, right? But what about `$arr1`, where we had alphabetic string literal keys `"foo"` and `"bar"`? In PHP, casting string literals containing non-numeric alphabets into an integer will return `0`, so if integer casting was all it did, `$arr1` wouldn't have string literal keys at all.

In fact, what it really does is __hashing__. For string keys, PHP computes a numeric hash, and uses the hash as the key to map to the given value. So a hash table is maintained within the array data type. Hash tables can (and eventually will) cause key collision due to the pigeon hole principle, but PHP arrays still need to store all pairs with unique keys given to it (you would want your "array" to append data and not overwrite values on every key collision). So the value actually points to a linked list containing all the value occurrences of the same key to preserve data.

So why have this unconventional, somewhat complicated data type in the first place? What do we gain over regular arrays? Its rather simple. We get a hash table we can loop through. Think about searching for a specific element in a regular array. Thats an __O(n)__ operation, where you have no other option but to simply go through each item to see if it matches the value you were looking for. In a PHP array... well its still an __O(n)__ operation because a poor hash function can lead to all keys converging into a single bucket, which then you would have to traverse through the linked list in O(n). But, as illustrated in this [Answer](https://stackoverflow.com/a/2484455), realistic performance benefits of using a hash table do exist. You also get to iterate through it, just like an array.

However, there is a tradeoff with all this performance benefit, and thats space overhead. Maintaining a dynamically growing, iterable hash table requires a lot of prep work in the backend of PHP, and all that allocation of memory can lead to a substantial memory usage, as investigated deeply in this [Article](https://www.npopov.com/2011/12/12/How-big-are-PHP-arrays-really-Hint-BIG.html).



## PHPArray.java

In this lab, we will simplify the specifications and narrow the required functionality of the associative PHP array and create a more intuitive toy version of it, called __`PHPArray`__. The exact specs for __`PHPArray`__ are the following:

1. The hash table will dynamically grow twice as large as soon as we reach 50% capacity.

2. Our __`PHPArray`__ __*will*__ overwrite values upon key collisions, so each bucket in the hash table will contain only one (key, value) pair node.

3. Array key search is done by __"Linear Probing"__, which is a fancy way to say our hash table is actually just a regular array of (key, value) nodes and our search will be an incremental for-loop starting from the integer hash of the given key up until index `M-1`.


## Testing

```bash
$ cd #YOUR_LAB6_DIRECTORY
$ javac ./*.java
$ java PHPArray
```

- The __`PHPArray`__ static main method will run to completion out-of-the-box, but with values that do not meet the expected behavior as is shown in __`output.txt`__.
