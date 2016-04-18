<html>
    <link rel="stylesheet" href="css/amazeui.min.css"/>
    <link rel="stylesheet" href="css/my.css">
    <?php $query = $_POST['query'];
    if (empty($query)){
    echo '
    <body>
        <table width="500" height="70" border="0" align="center"  cellpadding="0" cellspacing="0">
        <form name="form1" action="index.php" method="POST" >
        <tr><td><h2>Flash ChemLit</h2></td></tr>
        <tr>
            <td width="2000" height="30" align="center"><p><input type="text" class="am-form-field am-radius"/></p></td>
        </tr>
        <tr>
            <td height="25" align="center"><input class="submit_btn" id="submit_btn" type="submit" name="submit" value="Search"></td>
        </tr>
        </table>
        <table width="500" height="25" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
            <td height="25" align="center">
                <p>Last Updated: '; $a=filemtime("cache.txt");
                echo date("Y-m-d H:i:s",$a);echo '
                </p>
            </td>
        </tr>
        </form>
        </table>
    </body>
    ';
    }
    else {
        # code...
    }
    ?>
</html>

