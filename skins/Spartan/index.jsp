<%@ taglib uri="http://www.jwi.de/jGallery/taglib" prefix="jg" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <meta content="text/html; charset=ISO-8859-1"
 http-equiv="content-type">
  <title>${folder.title}</title>
  <link href="${folder.stylePath}" rel=stylesheet>
</head>
<body>

${folder.title} 
<jg:if exists="${folder.totalIndexes}">(${folder.indexNum}/${folder.totalIndexes})</jg:if>



<jg:if exists="${folder.totalIndexes}">

<select style="border: 0px none;" onchange="window.location.href = this.options[this.selectedIndex].value">
   <jg:indexiterator>
   
	<option ${index.selected} value="${index.page}">Page ${index.number}</option>
   </jg:indexiterator>	
</select> 


	<!-- Previous index -->
	<jg:if exists="${folder.previousIndexPage}">
		<a href="${folder.previousIndexPage}">
		&lt;&lt; </a>
	</jg:if>

	<!-- Next index -->
	<jg:if exists="${folder.nextIndexPage}">
		<a href="${folder.nextIndexPage}">
		&gt;&gt;</a>
	</jg:if>
</jg:if>




<center>

<table>
<jg:rowiterator> 
	<tr>
	<jg:coliterator>
	
		<td>
	
		<a href="${image.closeupPath}">
			<img class="image" 
				src="${image.thumbPath}" 
				width="${image.thumbWidth}" 
				height="${image.thumbHeight}" 
		</a> 
		</td>
		
	</jg:coliterator>
	</tr>
</jg:rowiterator>
</table>		

</center>


<a href="${folder.generatorurl}" target="_blank"><small>generated by ${folder.generator}</small></a>
</body>
</html>