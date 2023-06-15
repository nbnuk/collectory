<%@ page import="au.org.ala.collectory.CollectoryTagLib" %>
<div class="public-metadata">
    <h4><g:message code="dataAccess.title"/></h4>
    <div class="dataAccess btn-group-vertical">
        <a href="${grailsApplication.config.biocacheUiURL}/occurrences/search?fq=annotationsUid:${instance.uid}" class="btn btn-default">
            <i class="glyphicon glyphicon-list"></i> <g:message code="dataAccess.view.annotated.records"/></a>
    </div>
</div>