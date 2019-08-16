FingerPrint
===================

### Overview

This is a program that takes exelfile as a input calculates working hour and breaktimes.

### How it works

Through various image processing techniques combined with numpy arrays and unity simulation platform


1. Take Input Exel file (extension must be 'xlsx')
2. Copy all name surname date and day
3. Calculates the eachday entering and quitting time
4. Calculate the Break Time (Biggest time)
5. Last Column is Working Hour

### Requirements

1. This project developed using java 8

### Used libraries

1. Java 8
2. Maven Repositories
3. Apache Poi
4. Common-cli

### Execution

To run the program:

Go to `out\artifacts\FingerPrint_jar` path

Double click on `FingerPrint.jar` file

It shows a window and give a input file

Double click on the output file


### Note 

Last day is not works properly because read the line with iterator it can not add the last field of day.

