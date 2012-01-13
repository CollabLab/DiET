

<h3>Please read this information sheet and scroll to the bottom of the page   (Page 2/3)</h3> 


<h4>Title of project: </h4>Investigation into the Effects of a Network-based Chat Tool on Human Dialogue and Problem-Solving.

Principal investigator: </h4> Dr. Gregory Mills, Department of Psychology, Stanford University

<h4>Purpose of Study</h4>We are investigating a chat program in order to study its effect on how people interact with each other, and would like to ask you to participate in a study using these two programs. Your task will be to solve a collaborative task which should take 20 min of your time. Afterwards we welcome any feedback about your experience using the program.


<h4>Procedures</h4>The conversation will be recorded and kept for study. The records will be anonymous: when you begin the task you will be asked to choose a nickname for the chat, and your real name will not be stored in the recording of the conversation.

<h4>Time involvement: </h4> Your participation will take 20 minutes

<h4>Payment: </h4> You will be paid $5 for playing the experiment for 20 minutes. If you complete the task in less than 20 minutes you will still be paid $5.


<h4>Risks</h4>There are no known risks involved in this experimental procedure.


<h4>Benefits </h4>There are no immediate benefits of this research.

<h4>Confidentiality </h4>Your results will be kept completely confidential. Your name will not appear in any publication or presentation. To maximize confidentiality, no identifying features of a participant will be requested and IP addresses will not be recorded. Further, any identifying information will be removed from the transcript.


<h4>Subjects rights</h4> If you have read this form and have decided to participate in this project, please understand your participation is voluntary and you have the right to withdraw your consent or discontinue participation at any time without penalty or loss of benefits to which you are otherwise entitled. The alternative is not to participate. You have the right to refuse to answer particular questions. Your individual privacy will be maintained in all published and written data resulting from the study


<h4>Contact information </h4>Questions: If you have any questions, concerns or complaints about this research, its procedures, risks and benefits, contact the Protocol Director, Gregory Mills (gjmills@stanford.edu), on (415) 933-4982.


<h4>Independent contact </h4>If you are not satisfied with how this study is being conducted, or if you have any concerns, complaints, or general questions about the research or your rights as a participant, please contact the Stanford Institutional Review Board (IRB) to speak to someone independent of the research team at (650)-723-2480 or toll free at 1-866-680-2906. You can also write to the Stanford IRB, Stanford University, Stanford, CA 94305-5401.


<form id="amt_contform" method="POST" action="page3.php">
	<input type="hidden" id="turkId" name="turkId" value="<?php echo $_REQUEST['turkId']; ?>">	
	<input type="hidden" id="assignmentId" name="assignmentId" value="<?php echo $_REQUEST['assignmentId'];  ?>">
	<input id="submitButton" type="submit" name="Begin" value="Begin" 
	<?php 
		if ($_REQUEST['turkId'] == "") {
			echo 'onclick="alert(\'Please accept this HIT before continuing.\'); return false;"';
		}
	?> 
	>
</form>

