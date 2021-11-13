<!doctype html>
<html>
    <head>
        <title>Not Authorized</title>
        <meta name="layout" content="main">
        <g:if env="development"><asset:stylesheet src="errors.css"/></g:if>
    </head>
    <body>
        <ul class="errors">
            <li>Not authorized (401)</li>
            <li>Path: ${request.forwardURI}</li>
        </ul>
    </body>
</html>
