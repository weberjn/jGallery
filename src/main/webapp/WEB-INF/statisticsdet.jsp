<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
  <title>jGallery - Statistics</title>
  <link rel="stylesheet" type="text/css" href="${context}/statistics.css" />
</head>




<body bgcolor="#FFFFFF">
<h3>jGallery - Statistics for ${date} ${time}</h3>


<p>


<table border="1" cellpadding="3" cellspacing="0">
<tbody>
<tr>
<td colspan="3" class="title">${folder}</td>

</tr>
<tr>
<td class="header-center"><small>Image</small>&nbsp;
</td>


<td class="header-center"><small>hits</small>&nbsp;
</td>



</tr>

<c:forEach var="image" items="${statistics}">


<tr>


<td class="row-left"><a href="${image.url}">${image.name}</a></td>


<td class="row-center">${image.hits}</td>


</tr>

</c:forEach>


</tbody>
</table>


<a href=".." title="Back">Back</a>


<table border="0" cellpadding="3" cellspacing="0">
<tbody>
<tr>

<td class="row-left"><small><a href="http://jgallery.dev.java.net/" title="http://jgallery.dev.java.net/">jGallery ${version}</a> Copyright &copy; 2004 Juergen Weber</small></td>


</tr>

</tbody>
</table>
</body>
</html>