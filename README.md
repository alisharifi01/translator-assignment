# TRANSLATOR

### Instruction 

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
#### 5 input files
#### each file = 2.7g
#### row-mapping.conf = 76mb
#### column-mapping.conf = 37b

#### lscpu
Architecture:          x86_64 

CPU(s):                4

Thread(s) per core:    2


## JVM Heap size = 2g
![table](https://user-images.githubusercontent.com/8441165/53532059-99f20e80-3b0a-11e9-8de4-be3afdcb43b3.jpg)
### Developers

Ali Sharifi   (alisharifi01@gmail.com)

