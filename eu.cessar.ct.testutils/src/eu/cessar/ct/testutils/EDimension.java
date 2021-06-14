package eu.cessar.ct.testutils;

public enum EDimension {
	SystemTime (2),
	UsedJavaHeap (3),
	WorkingSet (4),
	Committed (7),
	WorkingSetPeak (8),
	ElapsedProcess (9),
	UserTime (10),
	KernelTime (11),
	PageFaults (19),
	CPUTime (20),
	CommitLimit (22),
	CommitPeak (23),
	PhysicalMemory (24),
	PhysicalAvailable (25),
	SystemCache (26),
	KernelTotal (27),
	KernelPaged (28),
	KernelNonpaged (29),
	PageSize (30),
	HandleCount (31),
	ProcessCount (32),
	ThreadCount (33),
	GDIObjects (34),
	USERObjects (35),
	OpenHandles (36),
	CommitTotal (37),
	ReadCount (38),
	WriteCount (39),
	BytesRead (40),
	BytesWritten (41),
	HardPageFaults (42),
	SoftPageFaults (43),
	TextSize (44),
	DataSize (45),
	LibrarySize (46),
	UsedMemory (48),
	FreeMemory (49),
	BuffersMemory (50),
	FreeJavaMemory (51),
	InvocationCount (52),
	;
	
	private int dimension;

    EDimension(int dim)
    {
    	dimension = dim;
    }

    public int getCode()
    {
        return(dimension);
    }
}
