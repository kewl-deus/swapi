FROM logstash:7.4.1
COPY filebeat-pipeline.conf /usr/share/logstash/pipeline/logstash.conf
CMD /usr/share/logstash/bin/logstash
#CMD /usr/share/logstash/bin/logstash -f /usr/share/logstash/pipeline/logstash.conf