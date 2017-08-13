<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">Configuration</h1>
    </div>
</div>

<div class="row">

[#if !isInstalled]
    <div class="alert alert-danger">OnionCrawler has not been installed. Please set up database information in order to
        install it.
    </div>
[/#if]
[#if isSaved ??]
    <div class="alert alert-success">Configuration saved.</div>
[/#if]
    <form action="/admin/configuration" method="GET">
        <div class="panel panel-default">
            <div class="panel-heading">Database configuration</div>
            <div class="panel-body">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="form-group">
                            <label>Hostname</label>
                            <input class="form-control hostname" placeholder="example:localhost" name="hostname" [#if DatabaseHostname??]value="${DatabaseHostname}"[/#if]>
                        </div>
                        <div class="form-group">
                            <label>Port</label>
                            <input class="form-control port" placeholder="usually 3306" name="port" [#if DatabasePort??]value="${DatabasePort}"[/#if]>
                        </div>
                        <div class="form-group">
                            <label>Username</label>
                            <input class="form-control username" placeholder="" name="username" [#if DatabaseUsername??]value="${DatabaseUsername}"[/#if]>
                        </div>
                        <div class="form-group">
                            <label>Password</label>
                            <input class="form-control password" type="password" placeholder="" name="password" [#if DatabasePassword??]value="${DatabasePassword}"[/#if]>
                        </div>
                        <div class="form-group">
                            <label>Database name</label>
                            <input class="form-control database" placeholder="example: onionCrawler" name="database" [#if DatabaseName??]value="${DatabaseName}"[/#if]>
                        </div>
                        <button class="btn btn-warning" id="testDatabaseSettings" type="button">Test database settings
                        </button>
                    </div>
                </div>
            </div>

            <div class="panel-heading">General configuration</div>
            <div class="panel-body">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="form-group">
                            <label>Crawler user agent</label>
                            <input class="form-control userAgent" value="[#if CrawlerUserAgent??]${CrawlerUserAgent}[#else]OnionCrawler - https://github.com/Ueland/OnionCrawler[/#if]" name="userAgent">
                        </div>
                    </div>
                </div>
            </div>

            <div class="panel-heading">Tor configuration</div>
            <div class="panel-body">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="form-group">
                            <label>Tor socks proxy addresses. Format: IP/Hostname:Port - One entry per line. Tor opens a socks proxy on port 9050 by default.</label>
                            <textarea class="form-control socksProxies" name="socksProxies" rows="5">[#if SocksProxies??]${SocksProxies}[#else]localhost:9050[/#if]</textarea>
                        </div>
                    </div>
                </div>
            </div>

        </div>
        <button class="btn btn-primary" type="submit">Save changes [#if !isInstalled]and install system[/#if]</button>
    </form>

</div>
