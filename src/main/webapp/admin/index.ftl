<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">Admin Dashboard</h1>
    </div>
</div>

<div class="row">

</div>




<!-- /.col-lg-8 -->
<div class="col-lg-4">
    <div class="panel panel-default">
        <div class="panel-heading">
            <i class="fa fa-bell fa-fw"></i> Add URL for crawling
        </div>
        <div class="panel-body">
	        [#if URLToCrawlError??]
                <div class="alert alert-danger">${URLToCrawlError}</div>
	        [/#if]
	        [#if URLAdded??]
             <div class="alert alert-success">URL will be crawled.</div>
            [/#if]
			<p>Reminder: You can add both onion and clearnet URL`s. If the URL is to the clearnet, the URL will be scraped for onion URL`s that will be indexed later.</p>
            <form action="/admin/" method="GET">
                <div class="form-group">
                    <label>URL to crawl:</label>
                    <input class="form-control" name="URLToCrawl">
                    <p class="help-block">Example: http://foobar.onion</p>
                </div>
            </form>
        </div>
    </div>
</div>