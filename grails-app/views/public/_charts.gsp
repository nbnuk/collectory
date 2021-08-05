<asset:script type="text/javascript">
  var CHARTS_CONFIG = {
      biocacheServicesUrl: "${grailsApplication.config.biocacheServicesUrl}",
      biocacheWebappUrl: "${grailsApplication.config.biocacheUiURL}",
      bieWebappUrl: "${grailsApplication.config.bieUiURL}",
      collectionsUrl: "${grailsApplication.config.grails.serverURL}"
  };

  // records
  if (${!instance.hasProperty('resourceType') || instance.resourceType == 'records'}) {
      // summary biocache data
      var queryUrl = CHARTS_CONFIG.biocacheServicesUrl + "/occurrences/search.json?pageSize=0&q=${facet}:${instance.uid}";

      $.ajax({
        url: queryUrl,
        dataType: 'jsonp',
        timeout: 30000,
        complete: function(jqXHR, textStatus) {
            if (textStatus == 'timeout') {
                noData();
                alert('Sorry - the request was taking too long so it has been cancelled.');
            }
            if (textStatus == 'error') {
                noData();
                alert('Sorry - the records breakdowns are not available due to an error.');
            }
        },
        success: function(data) {
            // check for errors
            if (data.length == 0 || data.totalRecords == undefined || data.totalRecords == 0) {
                noData();
            } else {
                setNumbers(data.totalRecords);
                if (data.totalRecords > 0){
                    $('#dataAccessWrapper').css({display:'block'});
                    $('#totalRecordCountLink').html(data.totalRecords.toLocaleString() + " ${g.message(code: 'public.show.rt.des03')}");
                }
            }
        }
      });
  }

</asset:script>

<asset:script type="text/javascript">
    <charts:biocache
            biocacheServiceUrl="${grailsApplication.config.biocacheServicesUrl}"
            biocacheWebappUrl="${grailsApplication.config.biocacheUiURL}"
            q="${facet}:${instance.uid}"
            qc=""
            fq=""
    />
</asset:script>
