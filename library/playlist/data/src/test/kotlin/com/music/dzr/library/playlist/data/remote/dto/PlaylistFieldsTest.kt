package com.music.dzr.library.playlist.data.remote.dto

import kotlin.test.Test
import kotlin.test.assertEquals

class PlaylistFieldsTest {

    @Test
    fun constructor_createsSingleFieldExpression_whenCalledWithName() {
        val result = PlaylistFields(listOf(PlaylistField.NAME))
        assertEquals("name", result.value)
    }

    @Test
    fun constructor_createsNegatedField_whenCalledWithExcludedField() {
        val result = PlaylistFields(listOf(!PlaylistField.DESCRIPTION))
        assertEquals("!description", result.value)
    }

    @Test
    fun include_joinsMultipleFieldsWithComma_whenCalledWithList() {
        val result = PlaylistFields(
            listOf(PlaylistField.NAME, PlaylistField.DESCRIPTION, PlaylistField.URI)
        )
        assertEquals("name,description,uri", result.value)
    }

    @Test
    fun include_handlesSingleField_whenCalledWithSingleElement() {
        val result = PlaylistFields(listOf(PlaylistField.ID))
        assertEquals("id", result.value)
    }

    @Test
    fun include_handlesEmptyList_whenCalledWithEmptyList() {
        val result = PlaylistFields(emptyList())
        assertEquals("", result.value)
    }

    @Test
    fun includeExpr_joinsMultipleExpressionsWithComma_whenCalledWithList() {
        val nameExpr = PlaylistFields(listOf(PlaylistField.NAME))
        val descExpr = PlaylistFields(listOf(!PlaylistField.DESCRIPTION))
        
        val result = PlaylistFields.includeExpr(listOf(nameExpr, descExpr))
        assertEquals("name,!description", result.value)
    }

    @Test
    fun includeExpr_handlesSingleExpression_whenCalledWithSingleElement() {
        val nameExpr = PlaylistFields(listOf(PlaylistField.NAME))
        val result = PlaylistFields.includeExpr(listOf(nameExpr))
        assertEquals("name", result.value)
    }

    @Test
    fun includeExpr_handlesEmptyList_whenCalledWithEmptyList() {
        val result = PlaylistFields.includeExpr(emptyList())
        assertEquals("", result.value)
    }

    @Test
    fun path_createsDotSeparatedPath_whenCalledWithTwoParts() {
        val result = PlaylistFields.path(
            listOf(PlaylistField.OWNER, PlaylistField.ID)
        )
        assertEquals("owner.id", result.value)
    }

    @Test
    fun path_handlesThreeLevelPath_whenCalledWithThreeParts() {
        val result = PlaylistFields.path(
            listOf(PlaylistField.TRACK, PlaylistField.ALBUM, PlaylistField.NAME)
        )
        assertEquals("track.album.name", result.value)
    }

    @Test
    fun path_handlesSinglePart_whenCalledWithSingleElement() {
        val result = PlaylistFields.path(listOf(PlaylistField.NAME))
        assertEquals("name", result.value)
    }

    @Test
    fun path_handlesEmptyList_whenCalledWithEmptyList() {
        val result = PlaylistFields.path(emptyList())
        assertEquals("", result.value)
    }

    @Test
    fun items_createsItemsGroup_whenCalledWithExpressions() {
        val addedAtExpr = PlaylistFields(listOf(PlaylistField.ADDED_AT))
        val addedByExpr = PlaylistFields(listOf(PlaylistField.ADDED_BY))
        
        val result = PlaylistFields.items(listOf(addedAtExpr, addedByExpr))
        assertEquals("items(added_at,added_by)", result.value)
    }

    @Test
    fun items_handlesSingleExpression_whenCalledWithSingleElement() {
        val result = PlaylistFields.items(listOf(PlaylistFields(listOf(PlaylistField.ID))))
        assertEquals("items(id)", result.value)
    }

    @Test
    fun items_handlesEmptyList_whenCalledWithEmptyList() {
        val result = PlaylistFields.items(emptyList())
        assertEquals("items()", result.value)
    }

    @Test
    fun group_createsNamedGroupWithExpressions_whenCalledWithNameAndExpressions() {
        val trackName = PlaylistFields(listOf(PlaylistField.NAME))
        val trackHref = PlaylistFields(listOf(PlaylistField.HREF))
        
        val result = PlaylistFields.group(
            PlaylistField.TRACK,
            listOf(trackName, trackHref)
        )
        assertEquals("track(name,href)", result.value)
    }

    @Test
    fun group_handlesSingleExpression_whenCalledWithSingleElement() {
        val result = PlaylistFields.group(
            PlaylistField.ALBUM,
            listOf(PlaylistFields(listOf(PlaylistField.NAME)))
        )
        assertEquals("album(name)", result.value)
    }

    @Test
    fun group_handlesEmptyExpressions_whenCalledWithEmptyList() {
        val result = PlaylistFields.group(PlaylistField.TRACKS, emptyList())
        assertEquals("tracks()", result.value)
    }

    @Test
    fun complexNestedExpression_buildsCorrectly_whenConstructingTracksItemsTrackAlbum() {
        // Build: tracks.items(track(name,href,album(name,href)))
        val albumName = PlaylistFields(listOf(PlaylistField.NAME))
        val albumHref = PlaylistFields(listOf(PlaylistField.HREF))
        val albumGroup = PlaylistFields.group(PlaylistField.ALBUM, listOf(albumName, albumHref))
        
        val trackName = PlaylistFields(listOf(PlaylistField.NAME))
        val trackHref = PlaylistFields(listOf(PlaylistField.HREF))
        val trackGroup = PlaylistFields.group(
            PlaylistField.TRACK,
            listOf(trackName, trackHref, albumGroup)
        )
        
        val itemsGroup = PlaylistFields.items(listOf(trackGroup))
        val result = PlaylistFields.group(PlaylistField.TRACKS, listOf(itemsGroup))
        
        assertEquals("tracks(items(track(name,href,album(name,href))))", result.value)
    }

    @Test
    fun exclusionInComplexExpression_buildsCorrectly_whenExcludingAlbumName() {
        // Build: tracks.items(track(name,href,album(!name,href)))
        val albumNameExcluded = PlaylistFields(listOf(!PlaylistField.NAME))
        val albumHref = PlaylistFields(listOf(PlaylistField.HREF))
        val albumGroup = PlaylistFields.group(PlaylistField.ALBUM, listOf(albumNameExcluded, albumHref))
        
        val trackName = PlaylistFields(listOf(PlaylistField.NAME))
        val trackHref = PlaylistFields(listOf(PlaylistField.HREF))
        val trackGroup = PlaylistFields.group(
            PlaylistField.TRACK,
            listOf(trackName, trackHref, albumGroup)
        )
        
        val itemsGroup = PlaylistFields.items(listOf(trackGroup))
        val result = PlaylistFields.group(PlaylistField.TRACKS, listOf(itemsGroup))
        
        assertEquals("tracks(items(track(name,href,album(!name,href))))", result.value)
    }

    @Test
    fun toString_returnsValue_whenCalledOnPlaylistFields() {
        val fields = PlaylistFields(listOf(PlaylistField.NAME))
        assertEquals("name", fields.toString())
    }

    @Test
    fun realWorldPlaylistFieldsExample_buildsCorrectly_whenCombiningBasicAndTrackFields() {
        // Common use case: get playlist with basic info and track details
        val basicFields = PlaylistFields(
            listOf(PlaylistField.NAME, PlaylistField.DESCRIPTION, PlaylistField.IMAGES)
        )
        
        val trackFields = PlaylistFields.group(
            PlaylistField.TRACKS,
            listOf(
                PlaylistFields.items(
                    listOf(
                        PlaylistFields(listOf(PlaylistField.ADDED_AT)),
                        PlaylistFields.group(
                            PlaylistField.TRACK,
                            listOf(
                                PlaylistFields(listOf(PlaylistField.NAME, PlaylistField.HREF)),
                                PlaylistFields.group(
                                    PlaylistField.ALBUM,
                                    listOf(PlaylistFields(listOf(PlaylistField.NAME, PlaylistField.IMAGES)))
                                )
                            )
                        )
                    )
                )
            )
        )
        
        val combined = PlaylistFields.includeExpr(listOf(basicFields, trackFields))
        val expected = "name,description,images,tracks(items(added_at,track(name,href,album(name,images))))"
        
        assertEquals(expected, combined.value)
    }
}
