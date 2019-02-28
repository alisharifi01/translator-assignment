# TRANSLATOR

### Instruciton 

Use maven spring boot plugin to run project (in project root path)
```shell
 $ mvn spring-boot:run
```

Acording to row-mapping.conf(configuration file for column translation) 
and column-mapping.conf(configuration file for row translation) change the heap size 
(configuration file for column translation) 
```shell
mvn spring-boot:run -Drun.jvmArguments="-Xmx2g"
```
 
### Configs


Set input,output and config file paths in  ./src/main/resources/application.properties
You can find description for all fields in application.properties

### Design

![untitled diagram](https://user-images.githubusercontent.com/8441165/53450596-e9184080-3a31-11e9-945e-06d25519617e.jpg)

### Some Result
## 5 input files
## each file ------------> 2.7g
## row-mapping.conf -----> 76mb
## column-mapping.conf --> 37b

## lscpu
Architecture:          x86_64
CPU op-mode(s):        32-bit, 64-bit
Byte Order:            Little Endian
CPU(s):                4
On-line CPU(s) list:   0-3
Thread(s) per core:    2
Core(s) per socket:    2
Socket(s):             1
NUMA node(s):          1
Vendor ID:             GenuineIntel
CPU family:            6
Model:                 61
Model name:            Intel(R) Core(TM) i7-5500U CPU @ 2.40GHz
Stepping:              4
CPU MHz:               2731.499
CPU max MHz:           3000.0000
CPU min MHz:           500.0000
BogoMIPS:              4792.96
Virtualization:        VT-x
L1d cache:             32K
L1i cache:             32K
L2 cache:              256K
L3 cache:              4096K

## JVM Heap size = 2g

[Untitled spreadsheet - Sheet1.pdf](https://github.com/alisharifi01/translator-assignment/files/2912773/Untitled.spreadsheet.-.Sheet1.pdf)

### Developers

Ali Sharifi   (alisharifi01@gmail.com)

