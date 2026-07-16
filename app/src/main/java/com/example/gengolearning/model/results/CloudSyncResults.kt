package com.example.gengolearning.model.results

sealed interface CloudSyncResults {
    data object Success: CloudSyncResults
    data object Failure: CloudSyncResults
}