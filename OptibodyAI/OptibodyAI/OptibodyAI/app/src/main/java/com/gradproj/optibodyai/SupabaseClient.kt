package com.gradproj.optibodyai

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest

val supabase = createSupabaseClient(
    supabaseUrl = "https://bfdkjvaenjinlaocnlxn.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImJmZGtqdmFlbmppbmxhb2NubHhuIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzIyNTY3MDYsImV4cCI6MjA0NzgzMjcwNn0.bgleDqBb-CeHxLB4us7dvf_h7ZRRz0xPQMp78NJZ69M"
) {
    install(Postgrest)
    install(io.github.jan.supabase.auth.Auth)
}
