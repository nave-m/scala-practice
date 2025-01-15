package example.web

import zio.http.Client
import zio.http.Server

type ServerTestEnvironment = Server & Client & UserApi
object ServerTestEnvironment
