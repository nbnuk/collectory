<%@ page import="grails.converters.JSON; au.org.ala.collectory.ProviderGroup; au.org.ala.collectory.DataProvider" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="${layout ?: 'main'}" />
        <g:set var="entityName" value="${instance.ENTITY_TYPE}" />
        <g:set var="entityNameLower" value="${cl.controller(type: instance.ENTITY_TYPE)}"/>
        <title><g:message code="default.show.label" args="[entityName]" /></title>
        <script type="text/javascript" src="http://maps.google.com/maps/api/js?v=3.3&sensor=false"></script>
        <r:require modules="fileupload"/>
    </head>
    <body>
        <h1>Upload data file for:
            <g:link controller="dataResource" action="show" id="${instance.uid}">
            ${fieldValue(bean: instance, field: "name")}
            <cl:valueOrOtherwise value="${instance.acronym}"> (${fieldValue(bean: instance, field: "acronym")})</cl:valueOrOtherwise>
            </g:link>
        </h1>

        <div class="well pull-right span6">
            You can use this to upload a file.
        </div>

        <g:uploadForm action="uploadDataFile" controller="dataResource">

            <g:hiddenField name="id" value="${instance.uid}"/>

            <!-- drag and drop file uploads -->
            <label for="protocol">Protocol:</label>

            <g:select name="protocol" from="${connectionProfiles}" value="protocol"
                      optionValue="display" optionKey="name"/>

            <label for="fileToUpload">File:</label>

            <div class="fileupload fileupload-new" data-provides="fileupload">
              <div class="input-append">
                <div class="uneditable-input span3">
                  <i class="icon-file fileupload-exists"></i>
                  <span class="fileupload-preview"></span>
                </div>
                <span class="btn btn-file">
                  <span class="fileupload-new">Select file</span>
                  <span class="fileupload-exists">Change</span>
                  <input type="file" name="myFile" />
                </span>
                <a href="#" class="btn fileupload-exists" data-dismiss="fileupload">Remove</a>
              </div>
                <div style="clear:both">
                  <input type="submit" id="fileToUpload" class="btn fileupload-exists btn-primary" value="Upload"/>
                  <span class="btn cancel">Cancel</span>
                </div>
            </div>
        </g:uploadForm>

    </body>
</html>