use strict;
use File::Find;

my $cpr = <<END;
/*
	DbView - Graph Visualization
    Copyright (C) 2012  Denis BEURIVE

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

END

find(\&wanted, ('src'));

sub wanted
{
	my $directory = $File::Find::dir;
	my $file_name = $_;
	my $path      = $File::Find::name;
	my @lines     = ();

	if ($directory =~ m/\/tests$/) { next; }
	unless ($file_name =~ m/\.java$/) { next; }

	print "$path\n";
	unless (open(FD, "<${file_name}"))
	{
		print STDERR "Can not open file $path: $!\n";
		exit 1;
	}

	@lines = <FD>;

	close FD;

	unshift(@lines, $cpr);

	unlink ${file_name};

	unless (open(FD, ">${file_name}"))
	{
		print STDERR "Can not open file $path for writing: $!\n";
		exit 1;
	}

	print FD join("", @lines);

	close FD;
}


