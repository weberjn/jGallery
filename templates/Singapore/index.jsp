
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">


<head>

<meta http-equiv="Content-Type" content="text/html; charset=${folder.textEncoding}" />
<link rel="stylesheet" type="text/css" media="screen" href="${folder.templatePath}/main.css" />


<title>${folder.name}</title>



</head>

<body>


<div id="crumb">
You are here:

<c:forEach var="parent" items="${folder.parentFolderList}">

  <c:if test="${!parent.outOfContext}"> 
	<a href="<c:url value='${parent.url}'/>">${parent.name}</a> 
  </c:if> 
  
  <c:if test="${parent.outOfContext}"> 
	<a href="${parent.url}">${parent.name}</a> 
  </c:if> 

&gt;               

</c:forEach>






${folder.name}

</div>


<h2 class="sgTitle">


<c:choose> 
  <c:when test="${!empty folder.comment}"> 
    ${folder.comment}
  </c:when> 
  <c:otherwise> 
    ${folder.name}
  </c:otherwise> 
</c:choose>  


</h2>
<c:if test="${!empty folder.variables.copyright}"> 
	<h4 class="sgSubTitle">by ${folder.variables.copyright}</h4>
</c:if> 




<div class="sgShadow"><table class="sgShadow" cellspacing="0">
  <tr>
    <td><img src="${folder.templatePath}/images/shadow-tabl.gif" alt="" /></td>
    <td class="tabm"><table class="sgShadowTab" cellspacing="0"><tr><td>
  
    
    Showing ${folder.firstIndexImage}-${folder.lastIndexImage} of ${folder.totalImages} 
    
        <c:if test="${!empty folder.previousIndexPage}"> 
    	<a href="<c:url value='${folder.previousIndexPage}'/>">Previous</a> |  
  </c:if> 

    
    <c:if test="${!empty folder.parentIndexPage}"> 
         <c:if test="${!folder.parentIndexPage.outOfContext}"> 
	      <a href="<c:url value='${folder.parentIndexPage.url}'/>">Up</a>
         </c:if> 
  
         <c:if test="${folder.parentIndexPage.outOfContext}"> 
	      <a href="${folder.parentIndexPage.url}">Up</a>
         </c:if> 
  </c:if>  
    

<c:if test="${!empty folder.nextIndexPage}"> 
    	| <a href="<c:url value='${folder.nextIndexPage}'/>">Next</a> 
  </c:if> 
  
  
    </td><td><img src="${folder.templatePath}/images/shadow-tabr.gif" alt="" /></td></tr></table></td>
    <td class="tabr"><img src="${folder.templatePath}/images/blank.gif" alt="" /></td>
  </tr>
  <tr>
    <td class="tl"><img src="${folder.templatePath}/images/blank.gif" width="32" height="16" alt="" /></td>
    <td class="tm"><img src="${folder.templatePath}/images/blank.gif" alt="" /></td>
    <td class="tr"><img src="${folder.templatePath}/images/blank.gif" alt="" /></td>
  </tr>
  <tr>
    <td class="ml"><img src="${folder.templatePath}/images/blank.gif" alt="" /></td>
    <td class="mm">
  
    <c:forEach var="image" items="${folder.images}">
    <div class="sgThumbnail">
      <div class="sgThumbnailContent">
        <img class="borderTL" src="${folder.templatePath}/images/slide-tl.gif" alt="" />
        <img class="borderTR" src="${folder.templatePath}/images/slide-tr.gif" alt="" />
        
        <table><tr><td>
          <a href="<c:url value='${image.closeupPath}'/>">
			<c:choose> 
				<c:when test="${image.iconPath}"> 
					<img class="sgPreviewThumb"
					src="${image.iconPath}" 
					width="${image.thumbWidth}" height="${image.thumbHeight}"
					alt="" />
				</c:when> 
				<c:otherwise> 
					<img class="sgPreviewThumb"
					src="${image.thumbPath}" 
					width="${image.thumbWidth}" height="${image.thumbHeight}" 
					<c:if test="${!empty image.counterNI}">
					title="${image.counterNI} views. click to see large view"
					 </c:if>
					 <c:if test="${empty image.counterNI}">
					 title="click to see large view"
					 </c:if>
					alt="" />
				</c:otherwise> 
				
			</c:choose>  
		</a>
        </td></tr></table>
        
        <div class="roundedCornerSpacer">&nbsp;</div>
      </div>
      <div class="bottomCorners">
        <img class="borderBL" src="${folder.templatePath}/images/slide-bl.gif" alt="" />
        <img class="borderBR" src="${folder.templatePath}/images/slide-br.gif" alt="" />
      </div>
    </div>
    </c:forEach>
  
    </td>
    <td class="mr"><img src="${folder.templatePath}/images/blank.gif" alt="" /></td>
  </tr>
  <tr>
    <td class="bl"><img src="${folder.templatePath}/images/blank.gif" alt="" /></td>
    <td class="bm"><img src="${folder.templatePath}/images/blank.gif" alt="" /></td>
    <td class="br"><img src="${folder.templatePath}/images/blank.gif" width="32" height="32" alt="" /></td>
  </tr>
</table></div>
  
<div id="footer">
<c:if test="${!empty folder.comment}"> 
<p>
${folder.comment}
</p>
</c:if>  


<p> <a href="${folder.generatorurl}" target="_blank"><small>Powered by ${folder.generator}</small></a></p>


<c:if test="${!empty folder.counter}"> 
    	<small>folder viewed ${folder.counter} times</small>
  </c:if>  
</div>


</body></html>