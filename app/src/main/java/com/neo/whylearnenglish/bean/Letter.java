package com.neo.whylearnenglish.bean;

import java.io.Serializable;
import java.util.List;

/**
 *
 * Created by Neo on 2016/12/15.
 */

public class Letter implements Serializable{
    /**
     * 单词
     */
    public String key;

    public List<Pronunciation> pronunciationList;

    /**
     * 词类集合
     */
    public List<POS> posList;

    /**
     * 例句集合
     */
    public List<Sentence> sentenceList;

    /**
     * 发音
     */
    public static class Pronunciation implements Serializable{
        /**
         * 音标phonetic symbol
         */
        public String ps;
        /**
         * 发音
         */
        public String pron;

        @Override
        public String toString() {
            return "Pronunciation{" +
                    "ps='" + ps + '\'' +
                    ", pron='" + pron + '\'' +
                    '}';
        }
    }

    /**
     * 词类
     * parts of speech
     */
    public static class POS implements Serializable{
        public String posName;
        public String acceptation;

        @Override
        public String toString() {
            return "POS{" +
                    "posName='" + posName + '\'' +
                    ", acceptation='" + acceptation + '\'' +
                    '}';
        }
    }

    /**
     * 例句
     */
    public static class Sentence implements Serializable{
        public String orig;
        public String trans;

        @Override
        public String toString() {
            return "Sentence{" +
                    "orig='" + orig + '\'' +
                    ", trans='" + trans + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Letter{" +
                "key='" + key + '\'' +
                ", pronunciationList=" + pronunciationList.toString() +
                ", posList=" + posList.toString() +
                ", sentenceList=" + sentenceList.toString() +
                '}';
    }
}
/**
<dict num="219" id="219" name="219">
<key>wonder</key>
<ps>ˈwʌndə(r)</ps>
<pron>
http://res.iciba.com/resource/amp3/oxford/0/f3/6c/f36c81ffc562fe4d60268169587a2294.mp3
</pron>
<ps>ˈwʌndɚ</ps>
<pron>
http://res.iciba.com/resource/amp3/1/0/10/cf/10cf1fdf6ad958eeffa9853f6885cec9.mp3
</pron>
<pos>adj.</pos>
<acceptation>奇妙的；钦佩的；远超过预期的；</acceptation>
<pos>n.</pos>
<acceptation>惊奇；奇观；奇人；奇迹；</acceptation>
<pos>vt.</pos>
<acceptation>对…感到好奇；惊奇；感到诧异；想弄明白；</acceptation>
<pos>vi.</pos>
<acceptation>怀疑，想知道；惊讶；</acceptation>
<sent>
<orig>
Therefore, day after day, I wonder why, I wonder how, I wonder where you are.
</orig>
<trans>因此, 一天接着一天, 我纳闷为什么, 我纳闷如何, 我纳闷你在哪儿.</trans>
</sent>
<sent>
<orig>
Little wonder after his wonder goal against Chelsea's arch rivals, Spurs.
</orig>
<trans>他在对阵切尔西宿敌热刺取得令人赞叹的入球后, 他的反应亦毫不意外.</trans>
</sent>
<sent>
<orig>Do You Wonder Where Your Child - like Wonder Went?</orig>
<trans>想知道你孩子般的好奇心去了哪儿 吗 ?</trans>
</sent>
<sent>
<orig>
No wonder it is called the Eighth Wonder of the World.
</orig>
<trans>难怪被称为世界第八大奇迹呢.</trans>
</sent>
<sent>
<orig>Well , no wonder. - No wonder what?</orig>
<trans>怪不得. - 怪不得什么?</trans>
</sent>
</dict>*/