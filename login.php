<html>

<head>
<title>Login1 Example / PHP Submit Page</title>
<style>
body  { font-family:Verdana;
        color:maroon;
        background-color:aliceblue; }
input { font-family:Verdana; width="1in"; }
td    { text-align:center; }
</style>
</head>

<body>

<h1>Login1 Example / PHP Submit Page</h1>

<form action="display.php" method="post">
<p>
    <input type="text" size="10" name="username" />            <br />
    Username                                                   <br />
    <input type="password" size="10" name="passwd" />          <br />
    Password                                                   <br />
    <input type="submit" value="Submit" name="submitsutton"/>  
</p>
</form>

</body>
</html>
