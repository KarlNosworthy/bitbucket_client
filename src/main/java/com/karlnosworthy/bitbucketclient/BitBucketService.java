package com.karlnosworthy.bitbucketclient;

import com.karlnosworthy.bitbucketclient.model.BitBucketRepository;
import com.karlnosworthy.bitbucketclient.model.wrapper.BitBucketRepositoryWrapper;
import retrofit.http.*;

public interface BitBucketService {

    //
    // ===== Repositories =====
    //
    @GET("/repositories/{owner}")
    public BitBucketRepositoryWrapper getRepositories(@Path("owner") String owner);

    @GET("/repositories/{owner}")
    public BitBucketRepositoryWrapper getRepositories(@Path("owner") String owner, @Query("role") String role);

    @GET("/repositories/{team}")
    public BitBucketRepositoryWrapper getTeamRepositories(@Path("team") String team);

    @GET("/repositories/{team}")
    public BitBucketRepositoryWrapper getTeamRepositories(@Path("team") String owner, @Query("role") String role);



    @POST("/repositories/{owner}/{repo_slug}")
    public void createRepository(@Path("owner") String owner, @Path("repo_slug") String repoSlug, @Body BitBucketRepository repository);

}
