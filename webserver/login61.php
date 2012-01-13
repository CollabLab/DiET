#!/usr/local/bin/php








<html>


<head>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">



<?php echo $_SERVER["SERVER_NAME"];?>











<SCRIPT>








var tmpvar = "NOTINITIALIZED";

function setHTMLInputText(s){
   <!--document.forms[0].elements[1].value = s;-->
  tmpvar=s;
}

</SCRIPT>

<title>Chat tool</title>
</head>
<?php $assignment_id = $_REQUEST['assignmentId']; ?>
<?php $turk_id = $_REQUEST['turkId']; ?>
<br>

<li>Remember:</li>
<ul>
<li>You need to collaborate with your partner by typing using the chat tool</li>
<li>Try and finish the task as quickly as possible!</li>
<li>The game gets easier and easier as you go along</li>
<li>Please do not open any other webpages other than this one</li>
</b>
</ul>
<hr>
To start the chat window and wait for the other participant..press the button below. It should open up a new window that asks you to enter your participantID. Please enter the participantID assigned to you by the experimenter<p>

<applet code=diet.server.experimentmanager.EMStarterApplet.class 
      archive="/jarfile/chattool61.jar"
      width=160 height=80 MAYSCRIPT ALT="If you can't see a button here you won't be able to run the experiment...please choose to release the assignment! SORRY">
<PARAM name="userentersvalues" value= "yes">
<PARAM name="serveripaddress" value= <?php echo $_SERVER["SERVER_NAME"];?>>
</applet>



<br>
<br>
<hr>
<br>



</html>