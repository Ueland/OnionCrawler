<div class="row">
[#if errorCode == "404" ]
    <h1 class="page-header">Page not found</h1>

    <p>The page you are looking for could not be found.</p>
[#elseif errorCode == "503" ]
    <h1 class="page-header">Internal Server Error</h1>

    <p>Something has gone wrong on the server, please tell your system administrator to investigate the server logs to
        figure out what went wrong.</p>
[/#if]
</div>