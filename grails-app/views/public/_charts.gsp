<asset:script type="text/javascript">
    <charts:biocache
            biocacheServiceUrl="${grailsApplication.config.biocacheServicesUrl}"
            biocacheWebappUrl="${grailsApplication.config.biocacheUiURL}"
            q="${facet}:${instance.uid}"
            qc=""
            fq=""
            autoLoad="false"
    />
</asset:script>
