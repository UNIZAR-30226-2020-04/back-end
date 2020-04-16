package com.music.backend.service;

import com.music.backend.entity.Cancion;
import com.music.backend.entity.Podcast;

public interface PodcastService {

	public Boolean createPodcast(Podcast p) throws Exception;
	public Podcast getPodcast(int i, String s) throws Exception;
	public Boolean deletePodcast(int i, String s) throws Exception;
	public Boolean deleteByUser(String s) throws Exception;
	public Cancion[] listPodcast(int i, String s) throws Exception;
}
