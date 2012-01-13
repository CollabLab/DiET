#!/usr/local/bin/php

<html>
<head>



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
<li>You get paid $2.5 for taking part for 10 min</li>
<li>If you finish all the tasks quicker you will still be paid $2.5</li>
<li>So try and finish the task as quickly as possible!</li>
<li>The game gets easier and easier as you go along</li>
<li>Payment only to teams that have meaningful collaborative dialogues (You will get paid if you try to solve the task)</li>
<li>Because the task requires 2 people, you might need to wait until someone else logs in. You will be paid for waiting (unless someone else joins the game and you don't respond).</li>
</b>
</ul>
<hr>
To start the chat window and wait for the other participant..press the button below<p>

<applet code=diet.server.experimentmanager.EMStarterApplet.class 
      archive="/jarfile/chattool.jar"
      width=160 height=40 MAYSCRIPT ALT="If you can't see a button here you won't be able to run the experiment...please choose to release the assignment! SORRY">
      <PARAM name="participantId" value= <?php echo $_REQUEST['turkId'];?>>
      <PARAM name="assignmentId" value= <?php echo $_REQUEST['assignmentId'];?>>
</applet>



<br>
<br>
<hr>
<br>
After 10 minutes of playing the game, please press SUBMIT below. <br>
If you have any comments about the game, please do include them. We'll give a bonus for helpful comments. Thanks.


<br>
<form id="mturk_form" action="http://workersandbox.mturk.com/mturk/externalSubmit" method="get">
  <input type="hidden" name="assignmentId" value = "<?php echo $_REQUEST['assignmentId'];?>"/><br />
  <input type="hidden"  name="workerId" value = "<?php echo $_REQUEST['turkId'];?>"/><br />

  your comments:<BR>
                      <TEXTAREA NAME="comments" COLS=40 ROWS=5></TEXTAREA>


  <input type="submit" value="Submit" />
</form>



</html>