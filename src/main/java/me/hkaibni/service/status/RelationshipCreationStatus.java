package me.hkaibni.service.status;

public enum RelationshipCreationStatus {
    SUCCESS,
    INVALID_REQUEST,
    FAMILY_NOT_FOUND,
    PERSON_A_NOT_FOUND,
    PERSON_B_NOT_FOUND,
    PERSON_A_NOT_IN_FAMILY,
    PERSON_B_NOT_IN_FAMILY,
    SELF_RELATIONSHIP,
    ALREADY_EXISTS
}