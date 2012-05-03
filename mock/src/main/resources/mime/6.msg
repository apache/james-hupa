Delivered-To: nobody@foo.com
X-Sender: nobody@foo.com
X-Apparently-To: nobody@foo.com
Mime-Version: 1.0
Content-Type: multipart/mixed; boundary="001517402a4207c4230473156a2a"
From: Nobody <nobody@foo.com>
Date: Tue, 8 Sep 2009 15:48:17 -0300
Subject: [gwt-contrib] Looking for Incubator sponsor for ProgressBar
To: google-web-toolkit-contributors <gwt@foo.com>
Reply-To: en-usn_specialoffer-noreply-@newsletter.foo.com
Sender: nobody@foo.com
Precedence: bulk
X-Google-Loop: groups
Mailing-List: list nobody@foo.com;
    contact nobody@foo.com
List-Id: <google-web-toolkit-contributors.googlegroups.com>


--001517402a4207c4230473156a2a
Content-Type: multipart/alternative;
    boundary="001517402a4207c41d0473156a28"

--001517402a4207c41d0473156a28
Content-Type: text/plain; charset=ISO-8859-1

Hi,

I'm looking for incubator sponsor for ProgressBar in order to evaluate a new
method that simplifies the way used to change to progress bar style class.

Sometimes it's necessary to change the progress bar style attributes
(example: color) according the progress value.

if (progressBar.getProgress() > 7) {
//change the progress bar color to red
} else if (progressBar.getProgress() > 4){
//change the progress bar color to yellow
} else {
//change the progress bar color to green
}

The problem is that the ProgressBar has an element (barElement) that does
not expose this option.
In the current version it's necessary to access the child element in order
to change its attribute.
Example: progressBar.getElement().getFirstChildElement().setClassName("gwt-ProgressBar-red");

I created a simple method that allows the user to set the bar style class in
a easier way.
Example: progressBar.setBarClassName("gwt-ProgressBar-red");

Please see the file attached.
I would appreciate any feedback.

Thanks

--~--~---------~--~----~------------~-------~--~----~
http://groups.google.com/group/Google-Web-Toolkit-Contributors
-~----------~----~----~----~------~----~------~--~---


--001517402a4207c41d0473156a28
Content-Type: text/html; charset=ISO-8859-1
Content-Transfer-Encoding: quoted-printable

Hi,<br><br>I&#39;m looking for incubator sponsor for ProgressBar in order
to evaluate a new method that simplifies the way used to change to
progress bar style class.<br><br>Sometimes it&#39;s necessary to change the=
 progress bar style attributes (example: color) according the progress valu=
e. <br>
<br>if (progressBar.getProgress() &gt; 7) {<br>//change the progress bar co=
lor to red<br> } else if (progressBar.getProgress() &gt; 4){<br>//change th=
e progress bar color to yellow<br>} else {<br>//change the progress bar col=
or to green<br>


}<br><br>The problem is that the ProgressBar has an element (barElement) th=
at does not expose this option. <br>In the current version it&#39;s necessa=
ry to access the child element in order to change its attribute.<br>Example=
: progressBar.getElement().<div id=3D":3cy" class=3D"ii gt">

getFirstChildElement().setClassName(&quot;gwt-ProgressBar-red&quot;);<br>
<br>I created a simple method that allows the user to set the bar style cla=
ss in a easier way.<br>Example: progressBar.setBarClassName(&quot;gwt-Progr=
essBar-red&quot;);<br><br>Please see the file attached.<br>I would apprecia=
te any feedback.<br>


<br>Thanks</div><br>
--~--~---------~--~----~------------~-------~--~----~<br>
http://groups.google.com/group/Google-Web-Toolkit-Contributors<br>
-~----------~----~----~----~------~----~------~--~---<br>
<br>

--001517402a4207c41d0473156a28--

--001517402a4207c4230473156a2a
Content-Type: text/x-patch
Content-Transfer-Encoding: base64
Content-Disposition: attachment;
    filename="ProgressBar.java.diff"
X-Attachment-Id: f_fzczv1ps0

SW5kZXg6IHRydW5rLXJlYWQtb25seS9zcmMvY29tL2dvb2dsZS9nd3Qvd2lkZ2V0aWRlYXMvY2xp
ZW50L1Byb2dyZXNzQmFyLmphdmEKPT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09
PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PQotLS0gdHJ1bmstcmVhZC1vbmx5L3NyYy9j
b20vZ29vZ2xlL2d3dC93aWRnZXRpZGVhcy9jbGllbnQvUHJvZ3Jlc3NCYXIuamF2YQkocmV2aXNp
b24gMTcwOSkKKysrIHRydW5rLXJlYWQtb25seS9zcmMvY29tL2dvb2dsZS9nd3Qvd2lkZ2V0aWRl
YXMvY2xpZW50L1Byb2dyZXNzQmFyLmphdmEJKHdvcmtpbmcgY29weSkKQEAgLTM1NSw2ICszNTUs
MTMgQEAKICAgfQogCiAgIC8qKgorICAgKiBDaGFuZ2UgdGhlIGNsYXNzIGF0dHJpYnV0ZSBvZiB0
aGUgYmFyIGVsZW1lbnQuIAorICAgKi8KKyAgcHVibGljIHZvaWQgc2V0QmFyQ2xhc3NOYW1lKFN0
cmluZyBjbGFzc05hbWUpIHsKKyAgICBiYXJFbGVtZW50LnNldENsYXNzTmFtZShjbGFzc05hbWUp
OworICB9CisKKyAgLyoqCiAgICAqIEdldCB0aGUgdGV4dCBlbGVtZW50LgogICAgKiAKICAgICog
QHJldHVybiB0aGUgdGV4dCBlbGVtZW50Cg==
--001517402a4207c4230473156a2a--

