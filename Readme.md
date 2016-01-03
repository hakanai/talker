Talker (temporary name)
=======================

What is it?
-----------

A tool that reads comments from Ustream and speaks them out using VOCALOID.

But the way it's written makes it possible to add support for more sources of messages,
more voices, and arbitrary text replacement is possible.

Why wouldn't you just use Bouyomi-chan?
---------------------------------------

The only backend Bouyomi-chan has for driving VOCALOID to speak is Robokoe, AKA miku_speak.exe,
but this tool can only speak Japanese. You could put every single English word into a custom
dictionary perhaps, but no such dictionary existed.
And both these tools are closed source, making it impossible to fill in the gaps.

So we make use of some NLP libraries and duct tape to fill in the gaps before passing it off to
Robokoe.

Roadmap
-------

* Come up with a real name for the tool.
* Move configuration properly into the GUI.
* Add support for more message backends. Candidates:
    * Twitter
    * Niconico
    * IRC
* Add support for more speech backends. Candidates:
    * VOICEROID (drive the GUI? Or use an existing tool that drives the GUI?)
* Remove reliance on Robokoe. It should be possible to call a VST using JVstHost,
  but I found that library not to work and help does not appear to be forthcoming.
  Benefits if it can be made to work:
    * Better timing of syllables
    * Possibly even pitch variation
    * Support on Mac OS X (yes, VOCALOID runs on Macs now.)

Building
--------

There is no proper build in place yet. I have been running the GUI from IDEA.
However, everything is in pretty sensible locations so it should be fairly easy to get it to build.

Running
-------

The main entry point is `TalkerMain`. Running that will pop up a GUI. However, you will want to configure it first.

Configuring
-----------

Copy `config.json.example` to `config.json` and edit it.

**Ustream message provider**

    "messages": {
      "provider": "ustream",
      "channelId": 39393939
    },

Your channel ID is a number which I have been finding by viewing the source of the stream page and looking for a line like this:

    <meta name="ustream:channel_id" content="39393939">

Copy that value straight out.

**Mac speech provider (Mac OS X only)**

    "speech": {
      "provider": "mac",
      "voice": "Kyoko"
    },

There isn't anything particularly special you have to do to get this to work, other than be using a Mac.

**Robokoe speech provider (Windows only)**

    "speech": {
      "provider": "robokoe",
      "executable": "C:\\Program Files (x86)\\miku_speak\\miku_speak.exe"
    },

Install procedure:

* Install Miku:
    * I used VOCALOID2. The tool appears to support V1 as well, but I don't have it to test.
    * Activate the licence.
    * Start it up and check that it works.
* Install 初音ミクのロボ声:
    * Download from [Hatsune Wave](http://www.geocities.jp/hatsune_wave/) (scroll down to "初音ミクのロボ声").
    * Unzip it somewhere (I put it in C:\Program Files (x86)\miku_speak\).
* Install mecab
    * Download from [Mecab](http://mecab.googlecode.com/svn/trunk/mecab/doc/index.html) (scroll down to "Binary package for MS-Windows" or similar.)
    * Run the installer. I left everything at the default settings.
    * Put libmecab.dll on the path. I did it by copying it into the same dir you copied miku_speak into.
    * **TODO:** In theory this should leave things non-working because the mecab data is now in a place where the library can't find it. However, in practice, this setup appears to work. I'm not sure why. If you happen to know why, let me know!
* Open a Command Prompt, navigate to the directory containing miku_speak.exe and type something like:

        miku_speak.exe テスト

    You should get some sound. As long as this tool works, you're good to go.
* (Optional) Set up SAPI provider.
    * This isn't required to use miku_speak.exe directly, but sets up a SAPI provider so that you can use
      the alternative provider documented below.
    * Run setup.exe from the install directory.
    * Hit 追加 (Add). Adjust defaults if you want, hit OK.
    * Hit 終了 (Done)
    * After doing this, you might try to run SimpleTTS.js and get an error. This is normal.
      The developer didn't bother to install it into the 64-bit registry or even provide a 64-bit version.
      If you run it with the 32-bit Windows Scripting Host, then it should work fine.
      Other speech tools like Bouyomi-chan should also work fine since they are ancient 32-bit tools as well.

**SAPI speech provider (Windows only)**

    "speaker": {
      "provider": "sapi",
      "voice": "Miku",
      "rate": 4, // -10 ~ +10
      "force32Bit": true
    },

Windows ships with some speech providers out of the box so you should be able to use these.
The voice name here is matched against the exact name that is displayed for the voice in Windows.

The example here shows "Miku" being used as the voice, which will use Robokoe if you have followed
the additional instructions listed under the Robokoe section above.

The "force32Bit" option forces use of the WOW64 (32-bit "Windows on Windows64") version of the
Windows Script Host when running scripts. This gets around the issue of certain speech providers
not providing 64-bit versions.

