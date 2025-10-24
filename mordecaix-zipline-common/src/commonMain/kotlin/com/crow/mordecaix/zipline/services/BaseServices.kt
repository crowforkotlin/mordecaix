package com.crow.mordecaix.zipline.services

import app.cash.zipline.ZiplineService

interface BaseServices : ZiplineService { fun invoke() { } }