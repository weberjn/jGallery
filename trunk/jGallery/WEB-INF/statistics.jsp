<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
  <title>jGallery - Statistics</title>
  <link rel="stylesheet" type="text/css" href="statistics.css" />
</head>




<body bgcolor="#FFFFFF">
<h3>jGallery - Statistics for ${date} ${time}</h3>


<p>


<table border="1" cellpadding="3" cellspacing="0">
<tbody>
<tr>
<td colspan="4" class="title">

</tr>
<tr>
<td class="header-center"><small>Folder</small>&nbsp;
</td>

<td class="header-center"><small>hits</small>&nbsp;
</td>


<td class="header-center"><small>max/min Hits/Image</small>&nbsp;
</td>
<td class="header-center"><small>avg Hits/Image</small>&nbsp;
</td>
</tr>

<c:forEach var="folder" items="${statistics}">


<tr>


<td class="row-left"><a href="${folder.URL}" title="${folder.URL}">${folder.name}</a></td>

<td class="row-center">${folder.hits}</td>

<td class="row-center">${folder.max} / ${folder.min}</td>

<td class="row-center">${folder.avg}</td>



</tr>

</c:forEach>


</tbody>
</table>


<table border="0" cellpadding="3" cellspacing="0">
<tbody>
<tr>

<td class="row-left"><small><a href="http://jgallery.dev.java.net/" title="http://jgallery.dev.java.net/">jGallery ${version}</a> Copyright &copy; 2004 Juergen Weber</small></td>

<td class="row-right"><small>jGallery running on ${serverInfo}</small></td>

</tr>

</tbody>
</table>
</body>
</html>