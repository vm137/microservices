package com.epam.resource.service;

import com.epam.resource.exceptions.SongServiceException;
import com.epam.resource.model.dto.SongDto;
import lombok.RequiredArgsConstructor;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class SongService {
    
    @Value("${song-service.base-url}")
    private String songServiceBaseUrl;

    private final OkHttpClient client;

    private static final String SONG_NAME_TAG = "dc:title";
    private static final String SONG_ARTIST_TAG = "xmpDM:artist";
    private static final String SONG_ALBUM_TAG = "xmpDM:album";
    private static final String SONG_DURATION_TAG = "xmpDM:duration";
    private static final String SONG_YEAR_TAG = "xmpDM:releaseDate";

    public void storeMetadata(Long id, byte[] byteArray) {
        SongDto songDto = parseTags(byteArray);
        songDto.setId(id);

        Request request = new Request.Builder()
                .url(songServiceBaseUrl + "/songs/" + id)
                .build();

        Call call = client.newCall(request);
//        Response response2 = call.execute();

    }

    public SongDto parseTags(byte[] byteArray) {
        Metadata metadata = getMetadata(byteArray);
        String durationStr = metadata.get(SONG_DURATION_TAG);
        String durationFmt = convertDuration(durationStr);

        return SongDto.builder()
                .name(metadata.get(SONG_NAME_TAG))
                .artist(metadata.get(SONG_ARTIST_TAG))
                .album(metadata.get(SONG_ALBUM_TAG))
                .duration(durationFmt)
                .year(metadata.get(SONG_YEAR_TAG))
                .build();
    }

    private String convertDuration(String durationStr) {
        int seconds = (int) (Double.parseDouble(durationStr));
        int mm = (int) Math.floor(seconds / 60.);
        int ss = seconds - mm * 60;
        return String.format("%s:%s", mm, ss);
    }

    private static Metadata getMetadata(byte[] byteArray) {
        ContentHandler handler = new DefaultHandler();
        Metadata metadata = new Metadata();
        Mp3Parser parser = new Mp3Parser();
        ParseContext context = new ParseContext();

        InputStream dataStream = new ByteArrayInputStream(byteArray);

        try {
            parser.parse(dataStream, handler, metadata, context);
        } catch (IOException | SAXException e) {
            throw new SongServiceException("MP3 file parsing error");
        } catch (TikaException e) {
            throw new SongServiceException("Extract mp3 file metadata error");
        }
        return metadata;
    }
}
