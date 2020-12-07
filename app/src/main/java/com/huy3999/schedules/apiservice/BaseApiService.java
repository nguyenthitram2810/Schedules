package com.huy3999.schedules.apiservice;

import com.huy3999.schedules.model.CreateProjectInfo;
import com.huy3999.schedules.model.Project;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BaseApiService {
    @GET("projects/getProjectEmail/{email}")
    Observable<List<Project>> getAllProjects(@Path("email") String email);

    @POST("projects")
    Observable<String> createProject(@Body CreateProjectInfo project);

    @DELETE("projects/deleteProject/{id}")
    Observable<String> deleteProject(@Path("id") String id);

    @PUT("projects/updateProject/{id}")
    Observable<String> updateProject(@Path("id") String id, @Body CreateProjectInfo project);

    @GET("projects/getProject/{id}")
    Observable<Project> getProject(@Path("id") String id);
}
