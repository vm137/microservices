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

    // todo: move to properties
    public static final String BASE_URL = "http://localhost:8072";

    private final OkHttpClient client;

    static final String SONG_NAME_TAG = "dc:title";
    static final String SONG_ARTIST_TAG = "xmpDM:artist";
    static final String SONG_ALBUM_TAG = "xmpDM:album";
    static final String SONG_DURATION_TAG = "xmpDM:duration";
    static final String SONG_YEAR_TAG = "xmpDM:releaseDate";

    public SongDto parseTags(byte[] byteArray) {
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

        String durationStr = metadata.get(SONG_DURATION_TAG);

        // todo: convert to mm:ss
        String durationFmt = "mm:ss " + durationStr;

        return SongDto.builder()
                .name(metadata.get(SONG_NAME_TAG))
                .artist(metadata.get(SONG_ARTIST_TAG))
                .album(metadata.get(SONG_ALBUM_TAG))
                .duration(durationFmt)
                .year(metadata.get(SONG_YEAR_TAG))
                .build();
    }

    public void saveSongData(SongDto song) {
        Request request = new Request.Builder()
                .url(BASE_URL + "/songs/" + song.getId())
                .build();

        Call call = client.newCall(request);
//        Response response2 = call.execute();

    }
}
