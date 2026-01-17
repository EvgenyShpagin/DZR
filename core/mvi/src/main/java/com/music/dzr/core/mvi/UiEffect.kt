package com.music.dzr.core.mvi

/**
 * Represents one-time effects in the MVI pattern.
 *
 * Effects are used for actions that do not affect the state
 * but require a one-time operation, such as navigation,
 * showing a toast, or logging an analytics event.
 */
interface UiEffect
