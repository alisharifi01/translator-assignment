# EPAY

### Instruciton 

Use maven spring boot plugin to run project (in project root path)
```shell
 $ mvn spring-boot:run
```
 
### Configs

In ./src/main/resources you have config files

#thread pool size for file readers
#it should not be more than number of files
pool.data.file.parser=2

#thread pool size for consumers
pool.consumer.count=3

#sum of thread pools size should be equals to cpu cores or a bit more

#extension for output files
out.extension=out

#size of blocking queue which hold lines of file
queue.size=100000

#timout by milliseconds for poll operation if blocking queue
queue.polling.timeout=100

#row mapping file path
file.path.config.row.mapping=/home/alisharifi/Desktop/data/conf/row-mapping.conf

#column mapping file path
file.path.config.column.mapping=/home/alisharifi/Desktop/data/conf/column-mapping.conf

#input directory path
file.path.dir.input.data=/home/alisharifi/Desktop/data/input/

#output directory path
file.path.dir.output.data=/home/alisharifi/Desktop/data/output/


### Developers

Ali Sharifi   (alisharifi01@gmail.com)

