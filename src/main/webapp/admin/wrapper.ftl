<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>OnionCrawler</title>

    <!-- Core CSS - Include with every page -->
    <link href="/static/css/bootstrap.min.css" rel="stylesheet">
    <link href="/static/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="/static/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
    <link href="/static/css/plugins/timeline/timeline.css" rel="stylesheet">

    <!-- SB Admin CSS - Include with every page -->
    <link href="/static/css/sb-admin.css" rel="stylesheet">

    <link href="/static/css/onionCrawler.css" rel="stylesheet">

</head>

<body>
<div id="wrapper">

    <nav class="navbar navbar-default navbar-fixed-top" role="navigation" style="margin-bottom: 0">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".sidebar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/">OnionCrawler</a>
        </div>
        <!-- /.navbar-header -->

        <div class="navbar-default navbar-static-side" role="navigation">
            <div class="sidebar-collapse">
                <ul class="nav" id="side-menu">
                    <li>
                        <a href="/admin/"><i class="fa fa-dashboard fa-fw"></i> Admin Dashboard</a>
                    </li>
                    <li>
                        <a href="#"><i class="fa fa-wrench fa-fw"></i> Configuration<span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                            <li>
                                <a href="/admin/configuration"><i class="fa fa-wrench fa-fw"></i> System configuration</a>
                            </li>
                            <li>
                                <a href="https://github.com/Ueland/onioncrawler/issues/new"><i
                                        class="fa fa-bug fa-fw"></i> Report bug/feature request</a>
                            </li>
                        </ul>
                    </li>
                </ul>
                <!-- /#side-menu -->
            </div>
            <!-- /.sidebar-collapse -->
        </div>
        <!-- /.navbar-static-side -->
    </nav>
    <div id="page-wrapper">

        [@includeFrameContent /]

        <!-- /.row -->
    </div>
    <!-- /#page-wrapper -->

</div>
<!-- /#wrapper -->

<!-- Core Scripts - Include with every page -->
<script src="/static/js/jquery-1.10.2.js"></script>
<script src="/static/js/bootstrap.min.js"></script>
<script src="/static/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="/static/js/sb-admin.js"></script>

<script src="/static/js/onionCrawler.js"></script>

</body>

</html>

