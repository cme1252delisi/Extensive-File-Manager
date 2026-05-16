package tr.edu.deu.efm.core.impl.converter.engine.media.impl;

import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;

public class VideoMediaConverter extends BaseMediaConverter {

    @Override
    protected EncodingAttributes getEncodingAttributes(String targetExt) {
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat(targetExt);

        AudioAttributes audio = new AudioAttributes();
        attrs.setAudioAttributes(audio);

        VideoAttributes video = new VideoAttributes();
        attrs.setVideoAttributes(video);

        return attrs;
    }
}