using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace WCFTestApp
{
	class Program
	{
		static void Main(string[] args)
		{
			OrderServiceClient client = new OrderServiceClient();
			Console.WriteLine(client.HelloWho("Justin"));
			Console.Read();
			client.Close();
		}
	}
}
