<asset:script>
  // stats
  if(loadLoggerStats){
      if (${instance.resourceType == 'website'}) {
          loadDownloadStats("${grailsApplication.config.loggerURL}", "${instance.uid}","${instance.name}", "2000");
      } else if (${instance.resourceType == 'records'}) {
          loadDownloadStats("${grailsApplication.config.loggerURL}", "${instance.uid}","${instance.name}", "1002");
      }
  }
</asset:script>
