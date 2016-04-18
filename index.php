<html>
    <link rel="stylesheet" href="css/amazeui.min.css"/>
    <link rel="stylesheet" href="css/my.css">
    <style>
      .blog-footer {
        padding: 10px 0;
        text-align: center;
      }
    </style>
    <?php $query = $_POST['query'];
    if (empty($query)){
    echo '
    <body>
        <table width="500" height="70" border="0" align="center"  cellpadding="0" cellspacing="0">
        <form name="form1" action="index.php" method="POST" >
        <tr><td><h2>Flash ChemLit</h2></td></tr>
        <tr>
            <td width="2000" height="30" align="center"><p><input type="text" name="query" class="am-form-field am-radius"/></p></td>
        </tr>
        <tr>
            <td height="25" align="center"><input class="submit_btn" id="submit_btn" type="submit" name="submit" value="Search"></td>
        </tr>
        </table>
        <table width="500" height="25" border="0" align="center" cellpadding="0" cellspacing="0">
        </form>
        </table>
        <footer class="blog-footer">
          <p>Last Updated: '; $a=filemtime("cache.txt");
                echo date("Y-m-d H:i:s",$a);echo '<br/>
            <small>&#169; 2016 HANSI MOU ALL RIGHTS RESERVED</small>
          </p>
        </footer>
    </body>
    ';
    }
    else {
        echo '
<header class="am-topbar am-topbar-fixed-top">
  <div class="am-container">
    <h1 class="am-topbar-brand">
      <a href="" font-family="Optima">Flash ChemLit</a>
    </h1>
    
    <form action="index.php" method="POST" class="am-topbar-form am-topbar-left am-form-inline" role="search">
      <div class="am-form-group">
        <input type="text" class="am-form-field am-input-sm" value="';echo $query;echo '" size="55">
        <button type="submit" class="am-btn am-btn-primary am-btn-sm">SEARCH</button>
      </div>
    </form>
    
  </div>
</header>';
    }
    ?>
</html>

