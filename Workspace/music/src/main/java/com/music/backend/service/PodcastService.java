package com.music.backend.service;

import com.music.backend.entity.Podcast;

public interface PodcastService {

	public Boolean createPodcast(Podcast p) throws Exception;
}
