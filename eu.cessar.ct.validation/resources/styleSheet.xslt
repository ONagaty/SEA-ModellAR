<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- Edited by XMLSpy® -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
<html>
	<body>
	<table>
		<tr></tr>
  		<tr>
  			<td></td>
  			<td><font size="5">Validation Report</font></td>
  		</tr>
  		<tr></tr>
		<tr></tr> 	
	</table>
	<table border="1">
		<tr>
  			<td bgcolor="#D8D8D8" align="left">Number of Errors:</td>
  			<td><xsl:value-of select="count(ValidationReport/ValidationMessages/ValidationMessage[normalize-space(Severity)='ERROR'])"/></td>
  		</tr>
  		<tr>
  			<td bgcolor="#D8D8D8" align="left">Number of Warnings:</td>
  			<td><xsl:value-of select="count(ValidationReport/ValidationMessages/ValidationMessage[normalize-space(Severity)='WARNING'])"/></td>
  		</tr>
	</table>
 	<table>
  		<tr></tr>
  		<tr></tr>
  	</table>
 	<table style="table-layout: fixed; width: 1300" border="1">
      <tr bgcolor="#D8D8D8">
		<th width="300" align="center">Message</th>
		<th width="250" align="center">ObjectName</th>
		<th width="250" align="center">UriPath</th>
		<th width="200" align="center">ResourceName</th>
		<th width="200" align="center">eObjectType</th>
		<th width="100" align="center">Severity</th>
      </tr>
      <xsl:for-each select="ValidationReport/ValidationMessages/ValidationMessage">
      <tr>
        <td style="word-wrap: break-word"><xsl:value-of select="Message"/></td>
        <td style="word-wrap: break-word"><xsl:value-of select="ObjectName"/></td>
        <td style="word-wrap: break-word"><xsl:value-of select="UriPath"/></td>
        <td style="word-wrap: break-word"><xsl:value-of select="ResourceName"/></td>
        <td style="word-wrap: break-word"><xsl:value-of select="eObjectType"/></td>
        <td style="word-wrap: break-word"><xsl:value-of select="Severity"/></td>
      </tr>
      </xsl:for-each>
    </table>
    
  </body>
  </html>
</xsl:template>
</xsl:stylesheet>

