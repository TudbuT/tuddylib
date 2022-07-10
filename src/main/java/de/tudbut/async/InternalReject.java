package de.tudbut.async;

class InternalReject extends Error {
    Task<?> task;
	Reject real;

	InternalReject(Task<?> task, Reject real) {
		this.task = task;
		this.real = real;
    }
}
