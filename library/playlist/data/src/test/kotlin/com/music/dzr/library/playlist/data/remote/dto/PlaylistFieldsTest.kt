package com.music.dzr.library.playlist.data.remote.dto

import kotlin.test.Test
import kotlin.test.assertEquals

class PlaylistFieldsTest {

    @Test
    fun constructor_createsSingleFieldExpression_whenCalledWithName() {
        val result = PlaylistFields(listOf(PlaylistField.Name))
        assertEquals("name", result.value)
    }

    @Test
    fun constructor_createsNegatedField_whenCalledWithExcludedField() {
        val result = PlaylistFields(listOf(!PlaylistField.Description))
        assertEquals("!description", result.value)
    }

    @Test
    fun include_joinsMultipleFieldsWithComma_whenCalledWithList() {
        val result = PlaylistFields(
            listOf(PlaylistField.Name, PlaylistField.Description, PlaylistField.Uri)
        )
        assertEquals("name,description,uri", result.value)
    }

    @Test
    fun include_handlesSingleField_whenCalledWithSingleElement() {
        val result = PlaylistFields(listOf(PlaylistField.Id))
        assertEquals("id", result.value)
    }

    @Test
    fun include_handlesEmptyList_whenCalledWithEmptyList() {
        val result = PlaylistFields(emptyList())
        assertEquals("", result.value)
    }

    @Test
    fun includeExpr_joinsMultipleExpressionsWithComma_whenCalledWithList() {
        val nameExpr = PlaylistFields(listOf(PlaylistField.Name))
        val descExpr = PlaylistFields(listOf(!PlaylistField.Description))
        
        val result = PlaylistFields.includeExpr(listOf(nameExpr, descExpr))
        assertEquals("name,!description", result.value)
    }

    @Test
    fun includeExpr_handlesSingleExpression_whenCalledWithSingleElement() {
        val nameExpr = PlaylistFields(listOf(PlaylistField.Name))
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
            listOf(PlaylistField.Owner, PlaylistField.Id)
        )
        assertEquals("owner.id", result.value)
    }

    @Test
    fun path_handlesThreeLevelPath_whenCalledWithThreeParts() {
        val result = PlaylistFields.path(
            listOf(PlaylistField.Track, PlaylistField.Album, PlaylistField.Name)
        )
        assertEquals("track.album.name", result.value)
    }

    @Test
    fun path_handlesSinglePart_whenCalledWithSingleElement() {
        val result = PlaylistFields.path(listOf(PlaylistField.Name))
        assertEquals("name", result.value)
    }

    @Test
    fun path_handlesEmptyList_whenCalledWithEmptyList() {
        val result = PlaylistFields.path(emptyList())
        assertEquals("", result.value)
    }

    @Test
    fun items_createsItemsGroup_whenCalledWithExpressions() {
        val addedAtExpr = PlaylistFields(listOf(PlaylistField.AddedAt))
        val addedByExpr = PlaylistFields(listOf(PlaylistField.AddedBy))
        
        val result = PlaylistFields.items(listOf(addedAtExpr, addedByExpr))
        assertEquals("items(added_at,added_by)", result.value)
    }

    @Test
    fun items_handlesSingleExpression_whenCalledWithSingleElement() {
        val result = PlaylistFields.items(listOf(PlaylistFields(listOf(PlaylistField.Id))))
        assertEquals("items(id)", result.value)
    }

    @Test
    fun items_handlesEmptyList_whenCalledWithEmptyList() {
        val result = PlaylistFields.items(emptyList())
        assertEquals("items()", result.value)
    }

    @Test
    fun group_createsNamedGroupWithExpressions_whenCalledWithNameAndExpressions() {
        val trackName = PlaylistFields(listOf(PlaylistField.Name))
        val trackHref = PlaylistFields(listOf(PlaylistField.Href))
        
        val result = PlaylistFields.group(
            PlaylistField.Track,
            listOf(trackName, trackHref)
        )
        assertEquals("track(name,href)", result.value)
    }

    @Test
    fun group_handlesSingleExpression_whenCalledWithSingleElement() {
        val result = PlaylistFields.group(
            PlaylistField.Album,
            listOf(PlaylistFields(listOf(PlaylistField.Name)))
        )
        assertEquals("album(name)", result.value)
    }

    @Test
    fun group_handlesEmptyExpressions_whenCalledWithEmptyList() {
        val result = PlaylistFields.group(PlaylistField.Tracks, emptyList())
        assertEquals("tracks()", result.value)
    }

    @Test
    fun complexNestedExpression_buildsCorrectly_whenConstructingTracksItemsTrackAlbum() {
        // Build: tracks.items(track(name,href,album(name,href)))
        val albumName = PlaylistFields(listOf(PlaylistField.Name))
        val albumHref = PlaylistFields(listOf(PlaylistField.Href))
        val albumGroup = PlaylistFields.group(PlaylistField.Album, listOf(albumName, albumHref))
        
        val trackName = PlaylistFields(listOf(PlaylistField.Name))
        val trackHref = PlaylistFields(listOf(PlaylistField.Href))
        val trackGroup = PlaylistFields.group(
            PlaylistField.Track,
            listOf(trackName, trackHref, albumGroup)
        )
        
        val itemsGroup = PlaylistFields.items(listOf(trackGroup))
        val result = PlaylistFields.group(PlaylistField.Tracks, listOf(itemsGroup))
        
        assertEquals("tracks(items(track(name,href,album(name,href))))", result.value)
    }

    @Test
    fun exclusionInComplexExpression_buildsCorrectly_whenExcludingAlbumName() {
        // Build: tracks.items(track(name,href,album(!name,href)))
        val albumNameExcluded = PlaylistFields(listOf(!PlaylistField.Name))
        val albumHref = PlaylistFields(listOf(PlaylistField.Href))
        val albumGroup = PlaylistFields.group(PlaylistField.Album, listOf(albumNameExcluded, albumHref))
        
        val trackName = PlaylistFields(listOf(PlaylistField.Name))
        val trackHref = PlaylistFields(listOf(PlaylistField.Href))
        val trackGroup = PlaylistFields.group(
            PlaylistField.Track,
            listOf(trackName, trackHref, albumGroup)
        )
        
        val itemsGroup = PlaylistFields.items(listOf(trackGroup))
        val result = PlaylistFields.group(PlaylistField.Tracks, listOf(itemsGroup))
        
        assertEquals("tracks(items(track(name,href,album(!name,href))))", result.value)
    }

    @Test
    fun toString_returnsValue_whenCalledOnPlaylistFields() {
        val fields = PlaylistFields(listOf(PlaylistField.Name))
        assertEquals("name", fields.toString())
    }

    @Test
    fun realWorldPlaylistFieldsExample_buildsCorrectly_whenCombiningBasicAndTrackFields() {
        // Common use case: get playlist with basic info and track details
        val basicFields = PlaylistFields(
            listOf(PlaylistField.Name, PlaylistField.Description, PlaylistField.Images)
        )
        
        val trackFields = PlaylistFields.group(
            PlaylistField.Tracks,
            listOf(
                PlaylistFields.items(
                    listOf(
                        PlaylistFields(listOf(PlaylistField.AddedAt)),
                        PlaylistFields.group(
                            PlaylistField.Track,
                            listOf(
                                PlaylistFields(listOf(PlaylistField.Name, PlaylistField.Href)),
                                PlaylistFields.group(
                                    PlaylistField.Album,
                                    listOf(PlaylistFields(listOf(PlaylistField.Name, PlaylistField.Images)))
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
