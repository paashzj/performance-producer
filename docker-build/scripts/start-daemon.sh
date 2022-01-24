cd "$(dirname "$0")"

cd ..

echo `pwd`

mkdir /opt/sh/logs

# memory option
if [ ! -n "$HEAP_MEN" ]; then
  HEAP_MEN="1G"
fi
if [ ! -n "$DIR_MEN" ]; then
  HEAP_MEN="1G"
fi
JVM_OPT="-Xmx${HEAP_MEN} -Xms${HEAP_MEN} -XX:MaxDirectMemorySize=${DIR_MEN}"
JVM_OPT="${JVM_OPT} -XX:+UseG1GC -XX:MaxGCPauseMillis=10 -XX:+ParallelRefProcEnabled -XX:+UnlockExperimentalVMOptions -XX:+DoEscapeAnalysis -XX:ParallelGCThreads=4 -XX:ConcGCThreads=4"
# gc log option
JVM_OPT="${JVM_OPT} -Xlog:gc*=info,gc+phases=debug:/opt/sh/logs/gc.log:time,uptime:filecount=10,filesize=100M"

java $JAVA_OPT $JVM_OPT -jar /opt/sh/pf-producer.jar >>/opt/sh/logs/stdout.log 2>>/opt/sh/logs/stderr.log
